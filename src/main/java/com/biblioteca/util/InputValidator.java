package com.biblioteca.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation and sanitization.
 * Implements security best practices for input handling.
 */
public class InputValidator {
    
    // Regex patterns for validation
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{1,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+\\-\\s()]{7,20}$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[A-Za-z0-9\\s.,!?'\"\\-]{1,200}$");
    
    /**
     * Validate ID format (alphanumeric, underscore, hyphen only)
     */
    public static boolean isValidId(String id) {
        return id != null && ID_PATTERN.matcher(id).matches();
    }
    
    /**
     * Validate general string input (no special characters that could cause injection)
     */
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty() && 
               ALPHANUMERIC_PATTERN.matcher(input).matches();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Sanitize string input - remove potentially dangerous characters
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        
        // Remove leading/trailing whitespace
        String sanitized = input.trim();
        
        // Remove control characters
        sanitized = sanitized.replaceAll("[\\p{Cntrl}]", "");
        
        // Escape special characters that could be used in injection attacks
        sanitized = sanitized.replace("<", "&lt;")
                            .replace(">", "&gt;")
                            .replace("\"", "&quot;")
                            .replace("'", "&#x27;")
                            .replace("/", "&#x2F;");
        
        return sanitized;
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isValidPositiveInt(int value) {
        return value > 0;
    }
    
    /**
     * Validate string length
     */
    public static boolean isValidLength(String input, int minLength, int maxLength) {
        return input != null && 
               input.length() >= minLength && 
               input.length() <= maxLength;
    }
    
    /**
     * Check if string contains only alphanumeric characters
     */
    public static boolean isAlphanumeric(String input) {
        return input != null && input.matches("^[A-Za-z0-9]+$");
    }
    
    /**
     * Validate and sanitize combined
     */
    public static String validateAndSanitize(String input, String fieldName) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        
        String sanitized = sanitize(input);
        
        if (sanitized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " contains invalid characters");
        }
        
        return sanitized;
    }
}
