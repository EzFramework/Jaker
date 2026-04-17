package com.github.ezframework.jaker.providers;

import com.github.ezframework.jaker.ProviderContext;

import java.util.Random;

/**
 * Simple numeric utilities.
 */
public final class NumberProvider {

    private final ProviderContext ctx;

    public NumberProvider(final ProviderContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Return an integer between min and max (inclusive).
     *
     * @throws IllegalArgumentException when min &gt; max
     */
    public int numberBetween(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        final Random rnd = ctx.random();
        if (min == max) {
            return min;
        }
        return min + rnd.nextInt(max - min + 1);
    }

    /**
     * Return a pseudorandom double in the range [0.0, 1.0).
     */
    public double random() {
        return ctx.random().nextDouble();
    }

    /**
     * Return a pseudorandom integer in the range [0, bound).
     *
     * @param bound upper exclusive bound (must be greater than 0)
     * @return pseudorandom integer value
     */
    public int random(final int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be greater than 0");
        }
        return ctx.random().nextInt(bound);
    }

    /**
     * Return a pseudorandom double in the range [min, max).
     *
     * @param min lower inclusive bound
     * @param max upper exclusive bound
     * @return pseudorandom double value
     */
    public double random(final double min, final double max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be < max");
        }
        return min + (ctx.random().nextDouble() * (max - min));
    }

    /**
     * Return a pseudorandom long in the range [min, max].
     *
     * @param min lower inclusive bound
     * @param max upper inclusive bound
     * @return pseudorandom long value
     */
    public long randomLong(final long min, final long max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        if (min == max) {
            return min;
        }
        return ctx.random().nextLong((max - min) + 1) + min;
    }

    /**
     * Return a pseudorandom boolean value.
     *
     * @return pseudorandom boolean.
     */
    public boolean bool() {
        return ctx.random().nextBoolean();
    }
}
