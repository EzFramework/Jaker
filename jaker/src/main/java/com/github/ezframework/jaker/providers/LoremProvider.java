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
        "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua",
        "enim", "ad", "minim", "veniam", "quis", "nostrud", "exercitation",
        "ullamco", "laboris", "nisi", "aliquip", "ex", "ea", "commodo",
        "consequat", "duis", "aute", "irure", "in", "reprehenderit",
        "voluptate", "velit", "esse", "cillum", "fugiat", "nulla",
        "pariatur", "excepteur", "sint", "occaecat", "cupidatat", "non",
        "proident", "sunt", "culpa", "qui", "officia", "deserunt",
        "mollit", "anim", "id", "est", "laborum", "perspiciatis",
        "unde", "omnis", "iste", "natus", "accusantium", "doloremque",
        "laudantium", "totam", "rem", "aperiam", "eaque", "ipsa",
        "quae", "ab", "inventore", "veritatis", "quasi", "architecto"
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

    /**
     * Returns a lorem ipsum paragraph composed of a number of sentences.
     *
     * @return a paragraph of lorem ipsum text containing 3 to 5 sentences.
     */
    public String paragraph() {
        return paragraph(3 + random.nextInt(3));
    }

    /**
     * Returns a lorem ipsum paragraph composed of the given number of sentences.
     *
     * @param sentenceCount number of sentences in the paragraph
     * @return lorem ipsum paragraph
     */
    public String paragraph(final int sentenceCount) {
        if (sentenceCount <= 0) {
            throw new IllegalArgumentException("sentenceCount must be greater than 0");
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentenceCount; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sentence(5 + random.nextInt(6)));
        }
        return sb.toString();
    }
}
