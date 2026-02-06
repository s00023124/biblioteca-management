package com.biblioteca.iterator;

/**
 * Custom iterator interface for traversing collections.
 * Implements Iterator Pattern.
 */
public interface Iterator<T> {
    
    /**
     * Check if there are more elements to iterate
     */
    boolean hasNext();
    
    /**
     * Get the next element in the iteration
     */
    T next();
    
    /**
     * Reset iterator to the beginning
     */
    void reset();
    
    /**
     * Get current position in the collection
     */
    int getCurrentPosition();
}
