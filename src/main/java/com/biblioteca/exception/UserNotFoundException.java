package com.biblioteca.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends LibraryException {
    public UserNotFoundException(String userId) {
        super("User not found: " + userId, "USER_NOT_FOUND");
    }
}
