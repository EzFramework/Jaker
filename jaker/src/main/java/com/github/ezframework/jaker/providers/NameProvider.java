package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;
import com.github.ezframework.jaker.exceptions.MissingDataException;

public final class NameProvider {
    /** Locale tag (IETF) used for resource lookup. */
    private final String localeTag;

    /** Random generator. */
    private final Random random;

    /** Cached names dataset for configured locale. */
    private final List<String> names;

    public NameProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.localeTag = locale.toLanguageTag();
        this.random = random;
        this.names = loader.load(localeTag, "names");
    }

    /**
     * Return a random first name from the dataset for the configured locale.
     *
     * @return first name
     */
    public String firstName() {
        if (names.isEmpty()) {
            throw new MissingDataException("No names dataset for locale " + localeTag);
        }
        final int idx = random.nextInt(names.size());
        return names.get(idx);
    }

}
