package com.github.ezframework.jaker.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.ezframework.jaker.data.DataLoader;
import com.github.ezframework.jaker.exceptions.MissingDataException;

/**
 * Name provider variant that returns non-repeating values until exhausted.
 */
public final class UniqueNameProvider {

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag (IETF) used for resource lookup. */
    private final String localeTag;

    /** Random source used to shuffle the unique sequence. */
    private final Random random;

    /** Current unique sequence state, initialized on first use. */
    private volatile FirstNameState firstNameState;

    public UniqueNameProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
    }

    /**
     * Return the next unique first name for the configured locale.
     *
     * @return unique first name
     * @throws IllegalStateException when all unique names have been consumed
     */
    public String firstName() {
        final FirstNameState state = ensureFirstNameState();
        final int index = state.index.getAndIncrement();

        if (index >= state.names.size()) {
            throw new IllegalStateException("No unique first names remaining for locale " + localeTag);
        }
        return state.names.get(index);
    }

    /**
     * Reset the unique first-name sequence and reshuffle values.
     */
    public synchronized void reset() {
        firstNameState = null;
    }

    private FirstNameState ensureFirstNameState() {
        FirstNameState state = firstNameState;
        if (state != null) {
            return state;
        }

        synchronized (this) {
            state = firstNameState;
            if (state == null) {
                final List<String> list = loader.load(localeTag, "names");
                if (list.isEmpty()) {
                    throw new MissingDataException("No names dataset for locale " + localeTag);
                }
                final List<String> shuffled = new ArrayList<>(list);
                Collections.shuffle(shuffled, random);
                state = new FirstNameState(shuffled);
                firstNameState = state;
            }
            return state;
        }
    }

    private static final class FirstNameState {
        private final List<String> names;
        private final AtomicInteger index;

        private FirstNameState(final List<String> names) {
            this.names = names;
            this.index = new AtomicInteger(0);
        }
    }
}
