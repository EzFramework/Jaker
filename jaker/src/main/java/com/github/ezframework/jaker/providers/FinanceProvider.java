package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

public final class FinanceProvider {
    /** Fallback safe card number. */
    private static final String FALLBACK_SAFE_CARD = "4111-1111-1111-1111";

    /** Random instance. */
    private final Random random;

    /** Unsafe helper entry point. */
    private final UnsafeFinanceProvider unsafeProvider;

    /** Cached credit card dataset for configured locale. */
    private final List<String> creditCards;

    public FinanceProvider(final DataLoader loader, final Locale locale, final Random random) {
        final String localeTag = locale.toLanguageTag();
        this.random = random;
        this.creditCards = loader.load(localeTag, "creditcards");
        this.unsafeProvider = new UnsafeFinanceProvider(random);
    }

    /**
     * Return a credit card number for the configured locale.
     *
     * @return a credit card number from dataset or a simple fallback pattern.
     */
    public String creditCardNumber() {
        if (!creditCards.isEmpty()) {
            return creditCards.get(random.nextInt(creditCards.size()));
        }
        // simple fallback pattern
        return FALLBACK_SAFE_CARD;
    }

    /**
     * Access random/synthetic finance value generation.
     *
     * @return unsafe finance provider.
     */
    public UnsafeFinanceProvider unsafe() {
        return unsafeProvider;
    }

    /**
     * Unsafe finance helper that generates synthetic random card numbers.
     */
    public static final class UnsafeFinanceProvider {

        /** Issuer prefixes used for synthetic card generation. */
        private static final String[] IIN_PREFIXES = {
            "4", "51", "52", "53", "54", "55"
        };

        /** Card length. */
        private static final int CARD_LENGTH = 16;

        /** Random source. */
        private final Random random;

        UnsafeFinanceProvider(final Random random) {
            this.random = random;
        }

        /**
         * Return a Luhn-valid synthetic card number for test data.
         *
         * @return synthetic card number with separators.
         */
        public String creditCardNumber() {
            final String prefix = IIN_PREFIXES[random.nextInt(IIN_PREFIXES.length)];
            final StringBuilder digits = new StringBuilder(CARD_LENGTH);
            digits.append(prefix);
            while (digits.length() < CARD_LENGTH - 1) {
                digits.append(random.nextInt(10));
            }
            final int checksum = luhnCheckDigit(digits.toString());
            digits.append(checksum);
            return digits.substring(0, 4) + "-" + digits.substring(4, 8)
                + "-" + digits.substring(8, 12) + "-" + digits.substring(12, 16);
        }

        private int luhnCheckDigit(final String partial) {
            int sum = 0;
            boolean doubleDigit = true;
            for (int i = partial.length() - 1; i >= 0; i--) {
                int digit = partial.charAt(i) - '0';
                if (doubleDigit) {
                    digit *= 2;
                    if (digit > 9) {
                        digit -= 9;
                    }
                }
                sum += digit;
                doubleDigit = !doubleDigit;
            }
            return (10 - (sum % 10)) % 10;
        }
    }
}
