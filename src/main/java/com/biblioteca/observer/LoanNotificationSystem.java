package com.biblioteca.observer;

import com.biblioteca.util.LibraryLogger;
import java.util.ArrayList;
import java.util.List;

/**
 * Notification system for loan events.
 */
public class LoanNotificationSystem implements Subject {
    
    private final List<Observer> observers;
    private final LibraryLogger logger;
    
    public LoanNotificationSystem() {
        this.observers = new ArrayList<>();
        this.logger = LibraryLogger.getInstance();
    }
    
    @Override
    public void attach(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            logger.info("Observer attached: " + observer.getObserverName());
        }
    }
    
    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            logger.info("Observer detached: " + observer.getObserverName());
        }
    }
    
    @Override
    public void notifyObservers(String message) {
        logger.debug("Notifying " + observers.size() + " observers: " + message);
        for (Observer observer : observers) {
            try {
                observer.update(message);
            } catch (Exception e) {
                logger.error("Failed to notify observer: " + observer.getObserverName(), e);
            }
        }
    }
    
    /**
     * Notify about new loan
     */
    public void notifyLoanCreated(String userId, String documentId) {
        String message = String.format("New loan created - User: %s, Document: %s", 
                                      userId, documentId);
        notifyObservers(message);
    }
    
    /**
     * Notify about loan return
     */
    public void notifyLoanReturned(String userId, String documentId) {
        String message = String.format("Document returned - User: %s, Document: %s", 
                                      userId, documentId);
        notifyObservers(message);
    }
    
    /**
     * Notify about overdue loan
     */
    public void notifyLoanOverdue(String userId, String documentId, int daysOverdue) {
        String message = String.format("OVERDUE - User: %s, Document: %s (%d days overdue)", 
                                      userId, documentId, daysOverdue);
        notifyObservers(message);
    }
    
    /**
     * Get number of registered observers
     */
    public int getObserverCount() {
        return observers.size();
    }
}
