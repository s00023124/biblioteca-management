package com.biblioteca.composite;

import com.biblioteca.model.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Leaf class in Composite Pattern.
 * Represents a single document without children.
 */
public class DocumentLeaf implements DocumentComponent {
    
    private final Document document;
    
    public DocumentLeaf(Document document) {
        this.document = document;
    }
    
    @Override
    public String getName() {
        return document.getTitle();
    }
    
    @Override
    public void add(DocumentComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf node");
    }
    
    @Override
    public void remove(DocumentComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf node");
    }
    
    @Override
    public List<DocumentComponent> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public List<Document> getDocuments() {
        List<Document> docs = new ArrayList<>();
        docs.add(document);
        return docs;
    }
    
    @Override
    public void display(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "- " + document.getTitle() + 
                          " (ID: " + document.getId() + ")");
    }
    
    @Override
    public boolean isComposite() {
        return false;
    }
    
    public Document getDocument() {
        return document;
    }
}
