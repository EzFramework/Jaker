package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Provider for fake fruit metadata values.
 */
public final class FruitProvider {
    private static final int NAME_INDEX = 0;
    private static final int CATEGORY_INDEX = 1;
    private static final int COLOR_INDEX = 2;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    public FruitProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
    }

    /**
     * Return a random fruit name.
     *
     * @return fruit name.
     */
    public String name() {
        return field(NAME_INDEX, "Apple");
    }

    /**
     * Return a random fruit category.
     *
     * @return fruit category.
     */
    public String category() {
        return field(CATEGORY_INDEX, "Pome");
    }

    /**
     * Return the primary color of a random fruit.
     *
     * @return color name.
     */
    public String color() {
        return field(COLOR_INDEX, "Red");
    }

    private String field(final int fieldIndex, final String fallback) {
        final List<String> list = loader.load(localeTag, "fruits");
        if (list.isEmpty()) {
            return fallback;
        }
        final String[] parts = list.get(random.nextInt(list.size())).split("\\|");
        if (parts.length <= fieldIndex) {
            return fallback;
        }
        final String value = parts[fieldIndex].trim();
        return value.isEmpty() ? fallback : value;
    }
}
