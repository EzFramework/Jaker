package com.github.ezframework.jaker;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.github.ezframework.jaker.data.DataLoader;
import com.github.ezframework.jaker.exceptions.LocaleNotSupportedException;

/**
 * Builder/factory for `Faker` instances.
 *
 * @author EzFramework
 * @version 1.0.0
 */
public final class Jaker {

    /** Underlying Faker instance. */
    private final Faker faker;

    private Jaker(final Locale locale, final Random random) {
        this.faker = new Faker(locale, random);
    }

    /**
     * Create a new builder for configuring a Jaker instance.
     *
     * @return a new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Return the configured Faker instance.
     *
     * @return the configured Faker.
     */
    public Faker faker() {
        return faker;
    }

    public static class Builder {

        /** Locale to use for created fakers. Defaults to en-US. */
        private Locale locale = Locale.forLanguageTag("en-US");

        /** Optional seed for deterministic generation. */
        private Long seed = null;

        /**
         * Set locale using a Locale instance.
         *
         * @param locale the locale to use
         * @return the builder.
         */
        public Builder locale(final Locale locale) {
            this.locale = locale;
            return this;
        }

        /**
         * Set locale by IETF BCP 47 language tag (e.g. "en-US").
         *
         * @param languageTag IETF language tag
         * @return the builder.
         */
        public Builder locale(final String languageTag) {
            this.locale = Locale.forLanguageTag(languageTag);
            return this;
        }

        /**
         * Set a seed for deterministic generation.
         *
         * @param seed seed value
         * @return the builder
         */
        public Builder seed(final long seed) {
            this.seed = seed;
            return this;
        }

        /**
         * Build the configured `Jaker` instance.
         *
         * @return configured Jaker
         */
        public Jaker build() {
            final Random rnd = (seed == null) ? ThreadLocalRandom.current() : new Random(seed);
            final String tag = locale.toLanguageTag();
            if (!DataLoader.localeAvailable(tag)) {
                throw new LocaleNotSupportedException(tag);
            }
            return new Jaker(locale, rnd);
        }
    }

    /**
     * Convenience non-deterministic global instance.
     *
     * @return global Jaker
     */
    public static Jaker global() {
        return new Jaker(Locale.getDefault(), ThreadLocalRandom.current());
    }
}
