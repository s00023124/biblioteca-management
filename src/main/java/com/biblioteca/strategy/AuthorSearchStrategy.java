package com.biblioteca.strategy;

import com.biblioteca.model.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search strategy that searches by author name
 */
public class AuthorSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Document> search(List<Document> documents, String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerQuery = query.toLowerCase().trim();
        
        return documents.stream()
            .filter(doc -> doc.getAuthor().toLowerCase().contains(lowerQuery))
            .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Author Search";
    }
}
