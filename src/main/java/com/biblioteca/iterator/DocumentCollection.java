package com.biblioteca.iterator;

import com.biblioteca.model.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of documents with iterator support.
 */
public class DocumentCollection implements Collection<Document> {
    
    private final List<Document> documents;
    
    public DocumentCollection() {
        this.documents = new ArrayList<>();
    }
    
    @Override
    public Iterator<Document> createIterator() {
        return new DocumentIterator(documents);
    }
    
    @Override
    public void add(Document document) {
        if (document != null) {
            documents.add(document);
        }
    }
    
    @Override
    public void remove(Document document) {
        documents.remove(document);
    }
    
    @Override
    public int size() {
        return documents.size();
    }
    
    @Override
    public boolean isEmpty() {
        return documents.isEmpty();
    }
    
    public Document get(int index) {
        if (index >= 0 && index < documents.size()) {
            return documents.get(index);
        }
        return null;
    }
    
    public void clear() {
        documents.clear();
    }
    
    public List<Document> getAll() {
        return new ArrayList<>(documents);
    }
}
