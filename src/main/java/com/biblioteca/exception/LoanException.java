package com.biblioteca.exception;

/**
 * Exception thrown when loan operations fail.
 */
public class LoanException extends LibraryException {
    public LoanException(String message) {
        super(message, "LOAN_ERROR");
    }
}
