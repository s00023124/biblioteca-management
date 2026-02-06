package com.biblioteca.composite;

import com.biblioteca.model.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Category that can contain documents and subcategories.
 */
public class DocumentCategory implements DocumentComponent {
    
    private final String categoryName;
    private final List<DocumentComponent> children;
    
    public DocumentCategory(String categoryName) {
        this.categoryName = categoryName;
        this.children = new ArrayList<>();
    }
    
    @Override
    public String getName() {
        return categoryName;
    }
    
    @Override
    public void add(DocumentComponent component) {
        children.add(component);
    }
    
    @Override
    public void remove(DocumentComponent component) {
        children.remove(component);
    }
    
    @Override
    public List<DocumentComponent> getChildren() {
        return new ArrayList<>(children);
    }
    
    @Override
    public List<Document> getDocuments() {
        List<Document> allDocuments = new ArrayList<>();
        for (DocumentComponent child : children) {
            allDocuments.addAll(child.getDocuments());
        }
        return allDocuments;
    }
    
    @Override
    public void display(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "+ " + categoryName + 
                          " (" + getDocuments().size() + " documents)");
        for (DocumentComponent child : children) {
            child.display(depth + 1);
        }
    }
    
    @Override
    public boolean isComposite() {
        return true;
    }
    
    /**
     * Get count of all documents in this category and subcategories
     */
    public int getTotalDocumentCount() {
        return getDocuments().size();
    }
    
    /**
     * Find a document by ID in this category tree
     */
    public Document findDocumentById(String id) {
        for (Document doc : getDocuments()) {
            if (doc.getId().equals(id)) {
                return doc;
            }
        }
        return null;
    }
}
