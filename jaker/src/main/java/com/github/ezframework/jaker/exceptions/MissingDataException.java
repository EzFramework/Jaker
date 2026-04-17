package com.github.ezframework.jaker.exceptions;

/**
 * Exception thrown when requested dataset is missing for a locale.
 *
 * @author EzFramework
 * @version 1.0.0
 */
public class MissingDataException extends RuntimeException {

    /**
     * Create a new MissingDataException with a message.
     *
     * @param message human-readable message
     */
    public MissingDataException(String message) {
        super(message);
    }
}
