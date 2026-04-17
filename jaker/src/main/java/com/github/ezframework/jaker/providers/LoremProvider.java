package com.github.ezframework.jaker.providers;

import java.util.Locale;
import java.util.Random;
import java.util.StringJoiner;

/**
 * Simple lorem ipsum provider.
 *
 * @author EzFramework
 * @version 1.0.0
 */
public final class LoremProvider {

    /** Locale context (reserved for future use). */
    private final Locale locale;
    /** Random source for deterministic output where applicable. */
    private final Random random;

    /** Basic latin words used by the provider. */
    private static final String[] WORDS = {
        "lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
        "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
        "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
    };

    public LoremProvider(final Locale locale, final Random random) {
        this.locale = locale;
        this.random = random;
    }

    /**
     * Returns a short lorem ipsum sentence.
     *
     * @return a short lorem ipsum sentence.
     */
    public String sentence() {
        return sentence(5);
    }

    /**
     * Returns a lorem ipsum sentence with a specific number of words.
     *
     * @param wordAmount amount of words to include in the sentence
     * @return lorem sentence containing {@code wordAmount} words
     */
    public String sentence(final int wordAmount) {
        if (wordAmount <= 0) {
            throw new IllegalArgumentException("wordAmount must be greater than 0");
        }
        final StringJoiner sentence = new StringJoiner(" ");
        for (int i = 0; i < wordAmount; i++) {
            sentence.add(word());
        }
        final String base = sentence.toString();
        return Character.toUpperCase(base.charAt(0)) + base.substring(1) + ".";
    }

    /**
     * Returns a single random lorem word.
     *
     * @return random word
     */
    public String word() {
        return WORDS[random.nextInt(WORDS.length)];
    }
}
