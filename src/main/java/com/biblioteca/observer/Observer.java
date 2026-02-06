package com.biblioteca.observer;

/**
 * Observer interface for Observer Pattern.
 * Defines contract for objects that want to be notified of events.
 */
public interface Observer {
    
    /**
     * Called when the observed subject has changed
     * 
     * @param message Notification message
     */
    void update(String message);
    
    /**
     * Get observer name/identifier
     */
    String getObserverName();
}
