package com.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Abstract base class for library documents.
 */
public abstract class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private boolean available;
    private DocumentType type;
    
    public enum DocumentType {
        BOOK, MAGAZINE, DVD, NEWSPAPER
    }
    
    public Document(String id, String title, String author, LocalDate publicationDate, DocumentType type) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.available = true;
        this.type = type;
    }
    
    public final String getDisplayInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== ").append(getDocumentTypeName()).append(" ===\n");
        info.append("ID: ").append(id).append("\n");
        info.append("Title: ").append(title).append("\n");
        info.append("Author: ").append(author).append("\n");
        info.append("Publication Date: ").append(publicationDate).append("\n");
        info.append(getSpecificInfo());
        info.append("Available: ").append(available ? "Yes" : "No").append("\n");
        return info.toString();
    }
    
    protected abstract String getSpecificInfo();
    
    protected abstract String getDocumentTypeName();
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public DocumentType getType() {
        return type;
    }
    
    public void setType(DocumentType type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("%s[id=%s, title=%s, available=%s]", 
            getClass().getSimpleName(), id, title, available);
    }
}
