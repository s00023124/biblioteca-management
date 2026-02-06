package com.biblioteca.strategy;

import com.biblioteca.model.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search strategy that searches by document ID
 */
public class IdSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Document> search(List<Document> documents, String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String trimmedQuery = query.trim();
        
        return documents.stream()
            .filter(doc -> doc.getId().equals(trimmedQuery))
            .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "ID Search";
    }
}
