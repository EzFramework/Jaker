package com.github.ezframework.jaker.providers;

import com.github.ezframework.jaker.ProviderContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * High-level person helper combining name and simple DOB generation.
 */
public final class PersonProvider {

    /** Fallback safe phone for en-US. */
    private static final String FALLBACK_SAFE_PHONE_EN_US = "12125550123";

    /** Fallback safe phone for en-GB. */
    private static final String FALLBACK_SAFE_PHONE_EN_GB = "447911123456";

    /** Fallback safe phone for fr. */
    private static final String FALLBACK_SAFE_PHONE_FR = "33123456789";

    /** Fallback safe phone for de. */
    private static final String FALLBACK_SAFE_PHONE_DE = "49301234567";

    /** Fallback safe phone for es. */
    private static final String FALLBACK_SAFE_PHONE_ES = "34911234567";

    /** Fallback safe phone for nl. */
    private static final String FALLBACK_SAFE_PHONE_NL = "31205551234";

    private final Random random;
    private final String localeTag;
    private final NameProvider nameProvider;
    private final List<String> names;
    private final List<String> surnames;
    private final List<String> companyNames;
    private final List<String> phones;
    private final UnsafePersonProvider unsafeProvider;

    public PersonProvider(final ProviderContext ctx) {
        this.random = ctx.random();
        this.localeTag = ctx.locale().toLanguageTag();

        final ProviderContext.ProviderAccess providers = ctx.providers();
        this.nameProvider = (providers != null) ? providers.name() : null;

        this.names = ctx.loader().load(localeTag, "names");
        this.surnames = ctx.loader().load(localeTag, "surnames");
        this.companyNames = ctx.loader().load(localeTag, "company_names");
        this.phones = ctx.loader().load(localeTag, "phones");

        this.unsafeProvider = new UnsafePersonProvider(random, localeTag);
    }

    /**
     * Return a first name using the configured `NameProvider` when available,
     * otherwise fall back to the raw `names` dataset or a constant.
     */
    public String firstname() {
        if (nameProvider != null) {
            return nameProvider.firstName();
        }
        if (!names.isEmpty()) {
            return names.get(random.nextInt(names.size()));
        }
        return "John";
    }

    /**
     * Return a last name sourced from a `surnames` dataset if available,
     * otherwise try to derive something sensible or fall back to "Doe".
     */
    public String lastname() {
        if (!surnames.isEmpty()) {
            return surnames.get(random.nextInt(surnames.size()));
        }
        if (!companyNames.isEmpty()) {
            final String cand = companyNames.get(random.nextInt(companyNames.size()));
            final String[] parts = cand.split("\\s+");
            if (parts.length > 0) {
                return parts[0];
            }
        }
        return "Doe";
    }

    /**
     * Return a composed full name combining a first name and last name.
     * This prefers the configured `NameProvider` and locale datasets when available.
     *
     * @return full name (e.g. "Jane Doe")
     */
    public String fullname() {
        return firstname() + " " + lastname();
    }

    /**
     * Return a pseudo-random date of birth between 18 and 90 years ago.
     * Formatted as ISO-8601 (yyyy-MM-dd).
     */
    public String dob() {
        final Random r = random;
        final int minAge = 18;
        final int maxAge = 90;
        final int age = minAge + r.nextInt(maxAge - minAge + 1);
        final int days = r.nextInt(365);
        final LocalDate date = LocalDate.now().minusYears(age).minusDays(days);
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Return a random UUID string for stable record identifiers.
     *
     * @return UUID in canonical string format
     */
    public String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Return a creation timestamp in ISO-8601 UTC format.
     *
     * @return timestamp string (for example: 2026-04-16T10:15:30Z)
     */
    public String createdTimestamp() {
        return Instant.now().toString();
    }

    /**
     * Return an update timestamp in ISO-8601 UTC format.
     *
     * @return timestamp string (for example: 2026-04-16T10:15:35Z)
     */
    public String updatedTimestamp() {
        return Instant.now().toString();
    }

    /**
     * Return a source marker describing where synthetic data comes from.
     *
     * @return data source name
     */
    public String source() {
        return "jaker";
    }

    /**
     * Return a safe-to-share example phone number from locale datasets.
     *
     * @return safe sample phone number.
     */
    public String phone() {
        if (!phones.isEmpty()) {
            return phones.get(random.nextInt(phones.size()));
        }
        return switch (localeTag) {
            case "en-GB" -> FALLBACK_SAFE_PHONE_EN_GB;
            case "fr" -> FALLBACK_SAFE_PHONE_FR;
            case "de" -> FALLBACK_SAFE_PHONE_DE;
            case "es" -> FALLBACK_SAFE_PHONE_ES;
            case "nl" -> FALLBACK_SAFE_PHONE_NL;
            case "en-US" -> FALLBACK_SAFE_PHONE_EN_US;
            default -> FALLBACK_SAFE_PHONE_EN_US;
        };
    }

    /**
     * Access locale-aware random/synthetic person values.
     *
     * @return unsafe person provider
     */
    public UnsafePersonProvider unsafe() {
        return unsafeProvider;
    }

    /**
     * Unsafe person helper that generates random locale-based phone numbers.
     */
    public static final class UnsafePersonProvider {

        private static final int TEN = 10;
        private static final int TWO_HUNDRED = 200;
        private static final int TEN_THOUSAND = 10_000;
        private static final int TEN_MILLION = 10_000_000;
        private static final int ONE_HUNDRED_MILLION = 100_000_000;

        /** Random source. */
        private final Random random;

        /** Locale tag used for formatting. */
        private final String localeTag;

        UnsafePersonProvider(final Random random, final String localeTag) {
            this.random = random;
            this.localeTag = localeTag;
        }

        /**
         * Return a random locale-shaped phone number for synthetic test data.
         *
         * @return synthetic locale-based phone number.
         */
        public String phone() {
            return switch (localeTag) {
                case "en-GB" -> "44" + (7_000_000_000L + random.nextInt(1_000_000_000));
                case "fr" -> "33" + (1 + random.nextInt(9))
                    + pad8(random.nextInt(ONE_HUNDRED_MILLION));
                case "de" -> "49" + (TEN + random.nextInt(90))
                    + pad7(random.nextInt(TEN_MILLION));
                case "es" -> "34" + (6 + random.nextInt(4))
                    + pad8(random.nextInt(ONE_HUNDRED_MILLION));
                case "nl" -> "31" + (1 + random.nextInt(9))
                    + pad8(random.nextInt(ONE_HUNDRED_MILLION));
                case "en-US" -> "1"
                    + pad3(TWO_HUNDRED + random.nextInt(800))
                    + pad3(TWO_HUNDRED + random.nextInt(800))
                    + pad4(random.nextInt(TEN_THOUSAND));
                default -> "1"
                    + pad3(TWO_HUNDRED + random.nextInt(800))
                    + pad3(TWO_HUNDRED + random.nextInt(800))
                    + pad4(random.nextInt(TEN_THOUSAND));
            };
        }

        private String pad3(final int value) {
            return zeroPadDecimal(value, 3);
        }

        private String pad4(final int value) {
            return zeroPadDecimal(value, 4);
        }

        private String pad7(final int value) {
            return zeroPadDecimal(value, 7);
        }

        private String pad8(final int value) {
            return zeroPadDecimal(value, 8);
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
