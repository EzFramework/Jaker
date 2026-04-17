package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Provider for fake game server metadata values.
 */
public final class GameServerProvider {
    private static final int GAME_INDEX = 0;
    private static final int IP_INDEX = 1;
    private static final int VERSION_INDEX = 2;
    private static final int MAX_PLAYERS_INDEX = 3;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    public GameServerProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
    }

    /**
     * Return a random game title.
     *
     * @return game title.
     */
    public String game() {
        return field(GAME_INDEX, "Minecraft");
    }

    /**
     * Return a random game server IP.
     *
     * @return IPv4 address.
     */
    public String ip() {
        return field(IP_INDEX, "203.0.113.1");
    }

    /**
     * Return a random game version.
     *
     * @return game version.
     */
    public String version() {
        return field(VERSION_INDEX, "1.0.0");
    }

    /**
     * Return the maximum player count for a random game server.
     *
     * @return max player count as integer.
     */
    public int playerCount() {
        final String raw = field(MAX_PLAYERS_INDEX, "32");
        try {
            return Integer.parseInt(raw);
        } catch (final NumberFormatException ignored) {
            return 32;
        }
    }

    private String field(final int fieldIndex, final String fallback) {
        final List<String> list = loader.load(localeTag, "game_servers");
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
