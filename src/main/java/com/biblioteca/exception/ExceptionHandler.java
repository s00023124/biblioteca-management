package com.biblioteca.exception;

import com.biblioteca.util.LibraryLogger;

/**
 * Exception Handler implementing Exception Shielding Pattern.
 * Shields users from technical error details and logs complete error information.
 */
public class ExceptionHandler {
    
    private static final LibraryLogger logger = LibraryLogger.getInstance();
    
    /**
     * Handle exception with shielding - logs technical details, returns user-friendly message
     * 
     * @param e The exception to handle
     * @return User-friendly error message
     */
    public static String handleException(Exception e) {
        // Log complete technical details
        logger.error("Exception occurred: " + e.getClass().getName(), e);
        
        // Return user-friendly message (shield technical details)
        if (e instanceof LibraryException) {
            LibraryException libEx = (LibraryException) e;
            logger.info("Error Code: " + libEx.getErrorCode());
            return libEx.getUserMessage();
        } else {
            // Shield unexpected exceptions completely
            logger.error("Unexpected exception: " + e.getMessage(), e);
            return "An unexpected error occurred. Please try again or contact support.";
        }
    }
    
    /**
     * Handle exception and wrap in LibraryException if needed
     * 
     * @param e The exception to wrap
     * @return LibraryException (original or wrapped)
     */
    public static LibraryException wrapException(Exception e) {
        if (e instanceof LibraryException) {
            return (LibraryException) e;
        }
        logger.error("Wrapping unexpected exception", e);
        return new LibraryException("Operation failed due to system error", e);
    }
    
    /**
     * Validate and throw exception if condition is false
     * 
     * @param condition Condition to check
     * @param message Error message if condition is false
     * @throws LibraryException if condition is false
     */
    public static void validate(boolean condition, String message) throws LibraryException {
        if (!condition) {
            LibraryException ex = new LibraryException(message);
            logger.warning("Validation failed: " + message);
            throw ex;
        }
    }
    
    /**
     * Log and rethrow exception (for debugging in development)
     * 
     * @param e Exception to log and rethrow
     * @throws LibraryException
     */
    public static void logAndRethrow(LibraryException e) throws LibraryException {
        logger.error("Rethrowing exception: " + e.getMessage(), e);
        throw e;
    }
}
