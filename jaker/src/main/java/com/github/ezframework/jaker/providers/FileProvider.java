package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Provider for fake file metadata values.
 */
public final class FileProvider {
    private static final int NAME_INDEX = 0;
    private static final int EXTENSION_INDEX = 1;
    private static final int SIZE_INDEX = 2;
    private static final int MIME_TYPE_INDEX = 3;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    public FileProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
    }

    /**
     * Return a random file name.
     *
     * @return file name.
     */
    public String name() {
        return field(NAME_INDEX, "document");
    }

    /**
     * Return a random file extension.
     *
     * @return file extension.
     */
    public String extension() {
        return field(EXTENSION_INDEX, "txt");
    }

    /**
     * Return a random file size (bytes).
     *
     * @return file size in bytes.
     */
    public long size() {
        final String raw = field(SIZE_INDEX, "1024");
        try {
            return Long.parseLong(raw);
        } catch (final NumberFormatException ignored) {
            return 1024L;
        }
    }

    /**
     * Return the MIME type of a random file entry.
     *
     * @return MIME type string.
     */
    public String mimeType() {
        final String raw = field(MIME_TYPE_INDEX, "application/octet-stream");
        return raw;
    }

    /**
     * Return a random file size in the requested range.
     *
     * @param minBytes inclusive lower bound
     * @param maxBytes inclusive upper bound
     * @return random file size in bytes.
     */
    public long size(final long minBytes, final long maxBytes) {
        if (minBytes < 0) {
            throw new IllegalArgumentException("minBytes must be >= 0");
        }
        if (maxBytes < minBytes) {
            throw new IllegalArgumentException("maxBytes must be >= minBytes");
        }
        if (minBytes == maxBytes) {
            return minBytes;
        }
        final long range = maxBytes - minBytes + 1;
        return minBytes + (long) Math.floor(random.nextDouble() * range);
    }

    private String field(final int fieldIndex, final String fallback) {
        final List<String> list = loader.load(localeTag, "files");
        if (list.isEmpty()) {
            return fallback;
        }
        final String line = list.get(random.nextInt(list.size()));
        int start = 0;
        for (int i = 0; i < fieldIndex; i++) {
            final int delimiterIndex = line.indexOf('|', start);
            if (delimiterIndex < 0) {
                return fallback;
            }
            start = delimiterIndex + 1;
        }
        final int end = line.indexOf('|', start);
        final String value = (end < 0 ? line.substring(start) : line.substring(start, end)).trim();
        return value.isEmpty() ? fallback : value;
    }
}
