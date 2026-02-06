package com.biblioteca.exception;

/**
 * Base exception for library operations.
 */
public class LibraryException extends Exception {
    
    private final String errorCode;
    
    public LibraryException(String message) {
        super(message);
        this.errorCode = "LIB_ERROR";
    }
    
    public LibraryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "LIB_ERROR";
    }
    
    public LibraryException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getUserMessage() {
        return getMessage();
    }
}
