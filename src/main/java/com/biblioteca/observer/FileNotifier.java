package com.biblioteca.observer;

import com.biblioteca.util.LibraryLogger;

/**
 * Concrete Observer that logs notifications to file
 */
public class FileNotifier implements Observer {
    
    private final String userId;
    private final LibraryLogger logger;
    
    public FileNotifier(String userId) {
        this.userId = userId;
        this.logger = LibraryLogger.getInstance();
    }
    
    @Override
    public void update(String message) {
        logger.info("Notification for user " + userId + ": " + message);
    }
    
    @Override
    public String getObserverName() {
        return "FileNotifier:" + userId;
    }
}
