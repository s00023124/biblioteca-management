package com.biblioteca.strategy;

import com.biblioteca.model.Document;
import java.util.List;

/**
 * Strategy interface for document search algorithms.
 * Implements Strategy Pattern to allow different search strategies.
 */
public interface SearchStrategy {
    
    /**
     * Search for documents based on the strategy's criteria
     * 
     * @param documents List of documents to search through
     * @param query Search query
     * @return List of matching documents
     */
    List<Document> search(List<Document> documents, String query);
    
    /**
     * Get the name of this search strategy
     */
    String getStrategyName();
}
