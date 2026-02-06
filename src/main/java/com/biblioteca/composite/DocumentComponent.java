package com.biblioteca.composite;

import com.biblioteca.model.Document;
import java.util.List;

/**
 * Component interface for Composite Pattern.
 * Allows uniform treatment of individual documents and categories.
 */
public interface DocumentComponent {
    
    /**
     * Get the name of this component
     */
    String getName();
    
    /**
     * Add a child component (only valid for composite objects)
     */
    void add(DocumentComponent component);
    
    /**
     * Remove a child component (only valid for composite objects)
     */
    void remove(DocumentComponent component);
    
    /**
     * Get all child components (only valid for composite objects)
     */
    List<DocumentComponent> getChildren();
    
    /**
     * Get all documents in this component (leaf returns single, composite returns all)
     */
    List<Document> getDocuments();
    
    /**
     * Display information about this component
     */
    void display(int depth);
    
    /**
     * Check if this is a composite (has children)
     */
    boolean isComposite();
}
