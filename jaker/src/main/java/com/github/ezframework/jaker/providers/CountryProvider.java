package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Provider for country metadata values.
 */
public final class CountryProvider {
    private static final int NAME_INDEX = 0;
    private static final int CODE_INDEX = 1;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    public CountryProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
    }

    /**
     * Return a random country name.
     *
     * @return country name.
     */
    public String name() {
        return field(NAME_INDEX, "United States");
    }

    /**
     * Return a random ISO 3166-1 alpha-2 country code.
     *
     * @return country code.
     */
    public String code() {
        return field(CODE_INDEX, "US");
    }

    private String field(final int fieldIndex, final String fallback) {
        final List<String> list = loader.load(localeTag, "countries");
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
