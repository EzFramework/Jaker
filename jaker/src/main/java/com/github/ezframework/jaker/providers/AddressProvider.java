package com.github.ezframework.jaker.providers;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.ezframework.jaker.data.DataLoader;

public final class AddressProvider {
    /** Locale tag used for lookups. */
    private final String localeTag;

    /** Random instance. */
    private final Random random;

    /** Cached city names dataset for configured locale. */
    private final List<String> cities;

    /** Cached street names dataset for configured locale. */
    private final List<String> streets;

    public AddressProvider(final DataLoader loader, final Locale locale, final Random random) {
        this.localeTag = locale.toLanguageTag();
        this.random = random;
        this.cities = loader.load(localeTag, "cities");
        this.streets = loader.load(localeTag, "streets");
    }

    /**
     * Return a random city name for the configured locale.
     *
     * @return a random city name or "Unknown" if dataset missing.
     */
    public String city() {
        if (cities.isEmpty()) {
            return "Unknown";
        }
        return cities.get(random.nextInt(cities.size()));
    }

    /**
     * Return a random street name for the configured locale.
     *
     * @return a random street name or a fallback.
     */
    public String street() {
        if (streets.isEmpty()) {
            return "Main St";
        }
        return streets.get(random.nextInt(streets.size()));
    }

    /**
     * Return a locale-aware postal/ZIP code.
     *
     * @return generated postal code string
     */
    public String zipCode() {
        if ("en-US".equals(localeTag)) {
            return pad5(random.nextInt(100000));
        }
        if ("en-GB".equals(localeTag)) {
            final char areaFirst = (char) ('A' + random.nextInt(26));
            final char areaSecond = (char) ('A' + random.nextInt(26));
            final int district = random.nextInt(10);
            final int sector = random.nextInt(10);
            final char unitFirst = (char) ('A' + random.nextInt(26));
            final char unitSecond = (char) ('A' + random.nextInt(26));
            return String.format("%c%c%d %d%c%c",
                areaFirst, areaSecond, district, sector, unitFirst, unitSecond);
        }
        if ("de".equals(localeTag)
            || "fr".equals(localeTag)
            || "es".equals(localeTag)) {
            return pad5(random.nextInt(100000));
        }
        if ("nl".equals(localeTag)) {
            final int digits = 1000 + random.nextInt(9000);
            final char first = (char) ('A' + random.nextInt(26));
            final char second = (char) ('A' + random.nextInt(26));
            return String.format("%d %c%c", digits, first, second);
        }
        return pad5(random.nextInt(100000));
    }

    private String pad5(final int value) {
        return zeroPadDecimal(value, 5);
    }

    private String zeroPadDecimal(final int value, final int width) {
        final String digits = Integer.toString(value);
        if (digits.length() >= width) {
            return digits;
        }
        return "0".repeat(width - digits.length()) + digits;
    }
}
