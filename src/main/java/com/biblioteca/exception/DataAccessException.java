package com.biblioteca.exception;

/**
 * Exception thrown for I/O operations failures.
 */
public class DataAccessException extends LibraryException {
    public DataAccessException(String message, Throwable cause) {
        super(message, "DATA_ACCESS_ERROR", cause);
    }
}
