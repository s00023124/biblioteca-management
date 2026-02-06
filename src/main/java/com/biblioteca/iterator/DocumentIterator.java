package com.biblioteca.iterator;

import com.biblioteca.model.Document;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator for document collections.
 */
public class DocumentIterator implements Iterator<Document> {
    
    private final List<Document> documents;
    private int currentPosition;
    
    public DocumentIterator(List<Document> documents) {
        this.documents = documents;
        this.currentPosition = 0;
    }
    
    @Override
    public boolean hasNext() {
        return currentPosition < documents.size();
    }
    
    @Override
    public Document next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more documents to iterate");
        }
        Document document = documents.get(currentPosition);
        currentPosition++;
        return document;
    }
    
    @Override
    public void reset() {
        currentPosition = 0;
    }
    
    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }
}
