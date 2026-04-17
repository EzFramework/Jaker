package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

public final class InternetProvider {
    /** Default fallback domain. */
    private static final String FALLBACK_DOMAIN = "example.com";

    /** Random instance. */
    private final Random random;

    /** Unsafe helper entry point. */
    private final UnsafeInternetProvider unsafeProvider;

    /** Cached domains dataset for configured locale. */
    private final List<String> domains;

    /** Cached names dataset for configured locale. */
    private final List<String> names;

    public InternetProvider(final DataLoader loader, final Locale locale, final Random random) {
        final String localeTag = locale.toLanguageTag();
        this.random = random;
        this.domains = loader.load(localeTag, "domains");
        this.names = loader.load(localeTag, "names");
        this.unsafeProvider = new UnsafeInternetProvider(loader, localeTag, random);
    }

    /**
     * Return a random domain for the configured locale.
     *
     * @return a random domain from dataset or example.com fallback.
     */
    public String domain() {
        if (domains.isEmpty()) {
            return FALLBACK_DOMAIN;
        }
        return domains.get(random.nextInt(domains.size()));
    }

    /**
     * Return a simple email address constructed from a name/local part and a domain.
     *
     * @return a simple email address using a name/local part and a domain.
     */
    public String email() {
        final String local = names.isEmpty()
            ? "user"
            : names.get(random.nextInt(names.size())).toLowerCase(Locale.ROOT);
        return local + "@" + domain();
    }

    /**
     * Return an email address using the provided local part.
     *
     * @param localPart local-part value before the @ sign
     * @return email address.
     */
    public String email(final String localPart) {
        if (localPart == null || localPart.isBlank()) {
            throw new IllegalArgumentException("localPart must not be blank");
        }
        return localPart.toLowerCase(Locale.ROOT) + "@" + domain();
    }

    /**
     * Access random/synthetic internet value generation.
     *
     * @return unsafe internet provider.
     */
    public UnsafeInternetProvider unsafe() {
        return unsafeProvider;
    }

    /**
     * Unsafe internet helper that generates synthetic random domains.
     */
    public static final class UnsafeInternetProvider {

        /** Default fallback top-level domains. */
        private static final String[] FALLBACK_TLDS = {
            "com", "net", "org", "io", "dev", "app"
        };

        /** Random source. */
        private final Random random;

        /** Cached domains dataset for configured locale. */
        private final List<String> domains;

        UnsafeInternetProvider(final DataLoader loader, final String localeTag, final Random random) {
            this.random = random;
            this.domains = loader.load(localeTag, "domains");
        }

        /**
         * Return a random synthetic domain for high-cardinality test data.
         *
         * @return random synthetic domain.
         */
        public String domain() {
            final String label = randomLabel();
            final String tld = randomTld();
            return label + "." + tld;
        }

        private String randomLabel() {
            final int length = 5 + random.nextInt(8);
            final String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
            final StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            return sb.toString();
        }

        private String randomTld() {
            if (!domains.isEmpty()) {
                final String candidate = domains.get(random.nextInt(domains.size()));
                final int idx = candidate.lastIndexOf('.');
                if (idx >= 0 && idx < candidate.length() - 1) {
                    return candidate.substring(idx + 1);
                }
            }
            return FALLBACK_TLDS[random.nextInt(FALLBACK_TLDS.length)];
        }
    }
}
