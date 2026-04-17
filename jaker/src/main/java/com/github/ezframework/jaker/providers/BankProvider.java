package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Bank-related helpers for safe example IBANs, format templates, and synthetic unsafe IBANs.
 *
 * <p>The default {@link #iban()} method returns values from locale datasets that should contain
 * only safe-to-share example IBANs published in banking documentation. For random synthetic test
 * identifiers, use {@link #unsafe()}.</p>
 *
 * @author EzFramework
 * @version 1.0.0
 */
public final class BankProvider {

    /** Default fallback safe example IBAN. */
    private static final String FALLBACK_SAFE_IBAN = "NL99ABNA0123456789";

    /** Default fallback bank format template. */
    private static final String FALLBACK_BANK_FORMAT = "CCkkBBBBXXXXXXXXXX";

    /** Base used for unsafe synthetic IBAN numeric generation. */
    private static final int IBAN_BASE = 10_000;

    /** Modulus used for the first unsafe IBAN segment. */
    private static final int IBAN_MOD1 = 97;

    /** Modulus used for the second unsafe IBAN segment. */
    private static final int IBAN_MOD2 = 100;

    /** Data loader for datasets. */
    private final DataLoader loader;

    /** Locale tag. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    /** Unsafe helper entry point. */
    private final UnsafeBankProvider unsafeProvider;

    public BankProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.loader = loader;
        this.localeTag = locale.toLanguageTag();
        this.random = random;
        this.unsafeProvider = new UnsafeBankProvider(random);
    }

    /**
     * Return a safe example IBAN from locale data.
     *
     * @return safe sample IBAN.
     */
    public String iban() {
        final List<String> list = loader.load(localeTag, "ibans");
        if (!list.isEmpty()) {
            return list.get(random.nextInt(list.size()));
        }
        return FALLBACK_SAFE_IBAN;
    }

    /**
     * Return a bank-format template string (IBAN structure notation).
     *
     * @return bank format template.
     */
    public String format() {
        final List<String> list = loader.load(localeTag, "bank_formats");
        if (!list.isEmpty()) {
            return list.get(random.nextInt(list.size()));
        }
        return FALLBACK_BANK_FORMAT;
    }

    /**
     * Access random/synthetic bank value generation.
     *
     * @return unsafe bank provider
     */
    public UnsafeBankProvider unsafe() {
        return unsafeProvider;
    }

    /**
     * Unsafe bank helper that generates synthetic random IBAN-like values.
     */
    public static final class UnsafeBankProvider {

        /** Random instance. */
        private final Random random;

        UnsafeBankProvider(final Random random) {
            this.random = random;
        }

        /**
         * Return a random IBAN-like string for synthetic test generation.
         *
         * @return random synthetic IBAN-like string.
         */
        public String iban() {
            final int num = Math.abs(random.nextInt()) % IBAN_BASE;
            final int part1 = num % IBAN_MOD1;
            final int part2 = num % IBAN_MOD2;
            return "DE" + pad2(part1) + "000000000000" + pad2(part2);
        }

        /**
         * Return a random IBAN format-like template for synthetic test generation.
         *
         * @return random synthetic format string.
         */
        public String format() {
            final String country = randomCountryCode();
            final int checksum = random.nextInt(100);
            final String bankCode = randomLetters(4);
            final int accountLength = 8 + random.nextInt(5);
            return country + pad2(checksum) + bankCode + "X".repeat(accountLength);
        }

        private String randomCountryCode() {
            return randomLetters(2);
        }

        private String randomLetters(final int length) {
            final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            final StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            return sb.toString();
        }

        private String pad2(final int value) {
            return zeroPadDecimal(value, 2);
        }

        private String zeroPadDecimal(final int value, final int width) {
            final String digits = Integer.toString(value);
            if (digits.length() >= width) {
                return digits;
            }
            return "0".repeat(width - digits.length()) + digits;
        }
    }
}
