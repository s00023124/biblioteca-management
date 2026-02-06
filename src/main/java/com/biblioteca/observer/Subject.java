package com.biblioteca.observer;

/**
 * Subject interface for Observer Pattern.
 * Defines contract for objects that are being observed.
 */
public interface Subject {
    
    /**
     * Register an observer
     */
    void attach(Observer observer);
    
    /**
     * Unregister an observer
     */
    void detach(Observer observer);
    
    /**
     * Notify all registered observers
     */
    void notifyObservers(String message);
}
