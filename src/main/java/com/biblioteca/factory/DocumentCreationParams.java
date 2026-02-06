package com.biblioteca.factory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Parameter object for document creation.
 * Implements Builder Pattern for flexible object construction.
 */
public class DocumentCreationParams {
    private final String id;
    private final String title;
    private final String author;
    private final LocalDate publicationDate;
    private final Map<String, String> extraParams;
    
    private DocumentCreationParams(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.author = builder.author;
        this.publicationDate = builder.publicationDate;
        this.extraParams = builder.extraParams;
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    
    public String getExtraParam(String key) {
        return extraParams.get(key);
    }
    
    /**
     * Builder class for constructing DocumentCreationParams
     */
    public static class Builder {
        private String id;
        private String title;
        private String author;
        private LocalDate publicationDate;
        private Map<String, String> extraParams = new HashMap<>();
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        
        public Builder author(String author) {
            this.author = author;
            return this;
        }
        
        public Builder publicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }
        
        public Builder addExtraParam(String key, String value) {
            this.extraParams.put(key, value);
            return this;
        }
        
        public DocumentCreationParams build() {
            return new DocumentCreationParams(this);
        }
    }
}
