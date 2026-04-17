package com.github.ezframework.jaker.exceptions;

/**
 * Thrown when a requested locale is not available (no dataset resources found).
 */
public class LocaleNotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LocaleNotSupportedException(final String localeTag) {
        super("Locale not supported: " + localeTag);
    }

    public LocaleNotSupportedException(final String localeTag, final String message) {
        super("Locale not supported: " + localeTag + " - " + message);
    }
}
