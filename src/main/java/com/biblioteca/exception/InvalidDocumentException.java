package com.biblioteca.exception;

/**
 * Exception thrown when document creation fails.
 */
public class InvalidDocumentException extends LibraryException {
    public InvalidDocumentException(String message) {
        super(message, "INVALID_DOC");
    }
}
