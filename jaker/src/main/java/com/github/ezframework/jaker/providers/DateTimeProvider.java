package com.github.ezframework.jaker.providers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

public final class DateTimeProvider {
    /** Locale context for date/time generation. */
    private final Locale locale;

    /** Random instance used for date selection. */
    private final Random random;

    public DateTimeProvider(Locale locale, Random random) {
        this.locale = locale;
        this.random = random;
    }

    /**
     * Returns a recent ISO-8601 date string.
     *
     * @return ISO date string.
     */
    public String isoDate() {
        return isoDate(5);
    }

    /**
     * Returns an ISO-8601 date within the last {@code yearsBack} years.
     *
     * @param yearsBack lookback window in years (must be greater than 0)
     * @return ISO date string.
     */
    public String isoDate(final int yearsBack) {
        if (yearsBack <= 0) {
            throw new IllegalArgumentException("yearsBack must be greater than 0");
        }
        final int daysBack = random.nextInt(365 * yearsBack);
        return LocalDate.now().minusDays(daysBack).toString();
    }

    /**
     * Returns an ISO-8601 date between the provided bounds (inclusive).
     *
     * @param start lower bound date
     * @param end upper bound date
     * @return ISO date string.
     */
    public String isoDateBetween(final LocalDate start, final LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end must be non-null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start must be <= end");
        }
        final long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        final long offset = (days == 0) ? 0 : random.nextLong(days + 1);
        return start.plusDays(offset).toString();
    }

    /**
     * Returns a recent ISO-8601 local date-time string.
     *
     * @return ISO local date-time string.
     */
    public String isoDateTime() {
        return isoDateTime(5);
    }

    /**
     * Returns an ISO-8601 local date-time within the last {@code yearsBack} years.
     *
     * @param yearsBack lookback window in years (must be greater than 0)
     * @return ISO local date-time string.
     */
    public String isoDateTime(final int yearsBack) {
        if (yearsBack <= 0) {
            throw new IllegalArgumentException("yearsBack must be greater than 0");
        }
        final long secondsBack = random.nextLong(365L * yearsBack * 24 * 60 * 60);
        return LocalDateTime.now().minusSeconds(secondsBack).toString();
    }
}
