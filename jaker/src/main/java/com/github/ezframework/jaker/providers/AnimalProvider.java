package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Provider for fake animal metadata values.
 */
public final class AnimalProvider {
    private static final int NAME_INDEX = 0;
    private static final int CATEGORY_INDEX = 1;
    private static final int GENDER_INDEX = 2;
    private static final int DIET_INDEX = 3;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    public AnimalProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
    }

    /**
     * Return a random animal name.
     *
     * @return animal name.
     */
    public String name() {
        return field(NAME_INDEX, "Fox");
    }

    /**
     * Return a random animal category.
     *
     * @return animal category.
     */
    public String category() {
        return field(CATEGORY_INDEX, "Mammal");
    }

    /**
     * Return a random grammatical gender value.
     *
     * @return gender value.
     */
    public String gender() {
        return field(GENDER_INDEX, "unknown");
    }

    /**
     * Return the dietary classification of a random animal.
     *
     * @return diet type (e.g. Carnivore, Herbivore, Omnivore).
     */
    public String diet() {
        return field(DIET_INDEX, "Omnivore");
    }

    private String field(final int fieldIndex, final String fallback) {
        final List<String> list = loader.load(localeTag, "animals");
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
