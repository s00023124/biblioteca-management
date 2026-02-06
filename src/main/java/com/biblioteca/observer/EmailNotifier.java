package com.biblioteca.observer;

import com.biblioteca.util.LibraryLogger;

/**
 * Concrete Observer that simulates email notifications
 */
public class EmailNotifier implements Observer {
    
    private final String email;
    private final LibraryLogger logger;
    
    public EmailNotifier(String email) {
        this.email = email;
        this.logger = LibraryLogger.getInstance();
    }
    
    @Override
    public void update(String message) {
        // Simulate sending email
        String emailMessage = String.format("To: %s\nSubject: Library Notification\n\n%s", 
                                           email, message);
        logger.info("Email notification sent to " + email);
        // In real system, would send actual email here
    }
    
    @Override
    public String getObserverName() {
        return "EmailNotifier:" + email;
    }
}
