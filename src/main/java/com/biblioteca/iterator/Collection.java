package com.biblioteca.iterator;

/**
 * Collection interface for Iterator Pattern.
 * Defines contract for collections that can be iterated.
 */
public interface Collection<T> {
    
    /**
     * Create an iterator for this collection
     */
    Iterator<T> createIterator();
    
    /**
     * Add an item to the collection
     */
    void add(T item);
    
    /**
     * Remove an item from the collection
     */
    void remove(T item);
    
    /**
     * Get the size of the collection
     */
    int size();
    
    /**
     * Check if collection is empty
     */
    boolean isEmpty();
}
