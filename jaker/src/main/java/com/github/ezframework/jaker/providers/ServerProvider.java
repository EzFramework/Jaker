package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

/**
 * Provider for common server/network fake values.
 */
public final class ServerProvider {
    /** Random instance. */
    private final Random random;

    /** Cached domains dataset for configured locale. */
    private final List<String> domains;

    public ServerProvider(final DataLoader loader, final Locale locale, final Random random) {
        final String localeTag = locale.toLanguageTag();
        this.random = random;
        this.domains = loader.load(localeTag, "domains");
    }

    /**
     * Return a random host name in the shape {@code server-####.<domain>}.
     *
     * @return randomized host name.
     */
    public String hostname() {
        return hostname(null);
    }

    /**
     * Return a random host name in the shape {@code server-####.<domain>} using
     * the provided domain when specified.
     *
     * @param domain domain suffix to force in generated hostname. If blank, a
     *               random locale domain is used.
     * @return randomized host name.
     */
    public String hostname(final String domain) {
        return hostname("server", domain);
    }

    /**
     * Return a random host name in the shape {@code <prefix>-####.<domain>}
     * using the provided prefix and domain options.
     *
     * @param prefix hostname prefix to use before random suffix. If blank,
     *               {@code server} is used.
     * @param domain domain suffix to force in generated hostname. If blank, a
     *               random locale domain is used.
     * @return randomized host name.
     */
    public String hostname(final String prefix, final String domain) {
        final String resolvedDomain;
        if (domain == null || domain.isBlank()) {
            resolvedDomain = domains.isEmpty() ? "example.com" : domains.get(random.nextInt(domains.size()));
        } else {
            resolvedDomain = domain;
        }
        final String resolvedPrefix = (prefix == null || prefix.isBlank()) ? "server" : prefix;
        final int suffix = 1000 + random.nextInt(9000);
        return resolvedPrefix + "-" + suffix + "." + resolvedDomain;
    }

    /**
     * Return a random private IP address.
     *
     * @return randomized private IPv4.
     */
    public String ip() {
        return ipv4();
    }

    /**
     * Return a random IP address in either IPv4 or IPv6 format.
     *
     * @param ipv6 whether to generate IPv6.
     * @return randomized IP string.
     */
    public String ip(final boolean ipv6) {
        return ipv6 ? ipv6() : ipv4();
    }

    /**
     * Return a random private IPv4 address.
     *
     * @return randomized private IPv4.
     */
    public String ipv4() {
        final int block = random.nextInt(3);
        if (block == 0) {
            return "10." + octet() + "." + octet() + "." + octet();
        }
        if (block == 1) {
            return "172." + (16 + random.nextInt(16)) + "." + octet() + "." + octet();
        }
        return "192.168." + octet() + "." + octet();
    }

    /**
     * Return a random unique-local IPv6 address (fd00::/8).
     *
     * @return randomized private IPv6.
     */
    public String ipv6() {
        return "fd" + hexByte() + ":" + hextet() + ":" + hextet() + ":" + hextet()
            + ":" + hextet() + ":" + hextet() + ":" + hextet() + ":" + hextet();
    }

    private int octet() {
        return random.nextInt(256);
    }

    private String hextet() {
        return hex4(random.nextInt(0x10000));
    }

    private String hexByte() {
        return hex2(random.nextInt(0x100));
    }

    private String hex2(final int value) {
        return zeroPadHex(value, 2);
    }

    private String hex4(final int value) {
        return zeroPadHex(value, 4);
    }

    private String zeroPadHex(final int value, final int width) {
        final String hex = Integer.toHexString(value);
        if (hex.length() >= width) {
            return hex;
        }
        return "0".repeat(width - hex.length()) + hex;
    }
}
