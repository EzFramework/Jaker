package com.github.ezframework.jaker.providers;

import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Entry point for unique (non-repeating) provider variants.
 */
public final class UniqueProvider {

    private final UniqueNameProvider nameProvider;

    public UniqueProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.nameProvider = new UniqueNameProvider(loader, locale, random);
    }

    /**
     * Unique-name provider.
     *
     * @return unique-name provider
     */
    public UniqueNameProvider name() {
        return nameProvider;
    }
}
