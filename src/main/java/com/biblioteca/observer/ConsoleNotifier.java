package com.biblioteca.observer;

/**
 * Concrete Observer that logs notifications to console
 */
public class ConsoleNotifier implements Observer {
    
    private final String name;
    
    public ConsoleNotifier(String name) {
        this.name = name;
    }
    
    @Override
    public void update(String message) {
        System.out.println("[NOTIFICATION - " + name + "] " + message);
    }
    
    @Override
    public String getObserverName() {
        return "ConsoleNotifier:" + name;
    }
}
