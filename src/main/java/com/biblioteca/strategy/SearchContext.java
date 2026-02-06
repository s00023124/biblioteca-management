package com.biblioteca.strategy;

import com.biblioteca.model.Document;
import java.util.List;

/**
 * Context for switching between search strategies.
 */
public class SearchContext {
    
    private SearchStrategy strategy;
    
    public SearchContext() {
        this.strategy = new TitleSearchStrategy();
    }
    
    public SearchContext(SearchStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(SearchStrategy strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
    }
    
    public List<Document> executeSearch(List<Document> documents, String query) {
        return strategy.search(documents, query);
    }
    
    public String getCurrentStrategyName() {
        return strategy.getStrategyName();
    }
}
