package com.github.ezframework.jaker.data;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Loads newline datasets from classpath resources. Supports optional compact binary packs
 * with the `.bin` extension. When a .bin pack is found, a lazy access list is returned
 * that reads strings on demand without eagerly splitting the entire data block.
 *
 * @author EzFramework
 * @version 1.0.0
 */
public final class DataLoader {

    /**
     * Shared cache for loaded datasets, scoped to the JVM/classloader so
     * multiple DataLoader/Faker instances reuse already loaded data.
     */
    private static final ConcurrentMap<String, List<String>> CACHE = new ConcurrentHashMap<>();

    /** Magic header for .bin dataset packs. */
    private static final byte[] MAGIC_HEADER = new byte[] {'J', 'A', 'K', 'R'};

    public List<String> load(final String localeTag, final String category) {
        final String key = localeTag + "/" + category;
        return CACHE.computeIfAbsent(key, k -> loadResource(localeTag, category));
    }

    /**
     * Quick check whether a minimal dataset exists for the given locale.
     * This checks for presence of either a `.bin` pack or a `.txt` file for the
     * `names` category which is considered the canonical indicator for locale data.
     *
     * @param localeTag IETF language tag (e.g. "en-US")
     * @return {@code true} when some dataset for the locale is available on the classpath
     */
    public static boolean localeAvailable(final String localeTag) {
        final ClassLoader cl = DataLoader.class.getClassLoader();
        final String txt = "data/" + localeTag + "/names.txt";
        final String bin = "data/" + localeTag + "/names.bin";
        return cl.getResource(txt) != null || cl.getResource(bin) != null;
    }

    private List<String> loadResource(final String localeTag, final String category) {
        final String binResource = "data/" + localeTag + "/" + category + ".bin";
        final InputStream bis = getClass().getClassLoader().getResourceAsStream(binResource);
        if (bis != null) {
            try (DataInputStream dis = new DataInputStream(bis)) {
                final byte[] magic = new byte[MAGIC_HEADER.length];
                dis.readFully(magic);
                if (Arrays.equals(magic, MAGIC_HEADER)) {
                    final int version = dis.readUnsignedByte();
                    final int count = dis.readInt();
                    final int[] offsets = new int[count];
                    for (int i = 0; i < count; i++) {
                        offsets[i] = dis.readInt();
                    }
                    final byte[] data = dis.readAllBytes();
                    return new BinaryStringList(data, offsets);
                }
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        final String resource = "data/" + localeTag + "/" + category + ".txt";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
        if (is == null) {
            return Collections.emptyList();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final List<String> items = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                items.add(line);
            }
            return Collections.unmodifiableList(items);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Lightweight List backed by a compact binary pack. Strings are decoded on demand.
     */
    private static final class BinaryStringList extends AbstractList<String> {

        /** Underlying binary blob containing newline-separated strings. */
        private final byte[] data;

        /** Offsets into the data blob for each string. */
        private final int[] offsets;

        /**
         * Lazily populated decoded strings cache.
         * Uses atomic publication so values decoded by one thread are visible to others.
         */
        private final AtomicReferenceArray<String> decodedCache;

        BinaryStringList(final byte[] data, final int[] offsets) {
            this.data = data;
            this.offsets = offsets;
            this.decodedCache = new AtomicReferenceArray<>(offsets.length);
        }

        @Override
        public String get(final int index) {
            if (index < 0 || index >= offsets.length) {
                throw new IndexOutOfBoundsException();
            }
            final String cached = decodedCache.get(index);
            if (cached != null) {
                return cached;
            }

            final int start = offsets[index];
            final int end = (index + 1 < offsets.length) ? offsets[index + 1] : data.length;
            // Trim trailing newline if present
            int len = end - start;
            if (len > 0 && data[start + len - 1] == '\n') {
                len--;
            }
            final String decoded = new String(data, start, len, StandardCharsets.UTF_8);
            if (decodedCache.compareAndSet(index, null, decoded)) {
                return decoded;
            }
            return decodedCache.get(index);
        }

        @Override
        public int size() {
            return offsets.length;
        }
    }
}
