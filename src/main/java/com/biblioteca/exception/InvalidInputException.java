package com.biblioteca.exception;

/**
 * Exception thrown for invalid input.
 */
public class InvalidInputException extends LibraryException {
    public InvalidInputException(String message) {
        super(message, "INVALID_INPUT");
    }
}
