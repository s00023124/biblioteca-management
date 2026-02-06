package com.biblioteca.factory;

import com.biblioteca.model.*;
import com.biblioteca.exception.InvalidDocumentException;
import com.biblioteca.util.InputValidator;

import java.time.LocalDate;

/**
 * Factory for creating library documents.
 * Singleton pattern implementation.
 */
public class DocumentFactory {
    
    private static DocumentFactory instance;
    
    private DocumentFactory() {
    }
    
    public static synchronized DocumentFactory getInstance() {
        if (instance == null) {
            instance = new DocumentFactory();
        }
        return instance;
    }
    
    public Document createDocument(Document.DocumentType type, DocumentCreationParams params) 
            throws InvalidDocumentException {
        
        validateCommonParams(params);
        
        switch (type) {
            case BOOK:
                return createBook(params);
            case MAGAZINE:
                return createMagazine(params);
            default:
                throw new InvalidDocumentException("Unsupported document type: " + type);
        }
    }
    
    private void validateCommonParams(DocumentCreationParams params) throws InvalidDocumentException {
        if (params == null) {
            throw new InvalidDocumentException("Document parameters cannot be null");
        }
        
        if (!InputValidator.isValidId(params.getId())) {
            throw new InvalidDocumentException("Invalid document ID");
        }
        
        if (!InputValidator.isValidString(params.getTitle())) {
            throw new InvalidDocumentException("Invalid document title");
        }
        
        if (!InputValidator.isValidString(params.getAuthor())) {
            throw new InvalidDocumentException("Invalid author name");
        }
        
        if (params.getPublicationDate() == null) {
            throw new InvalidDocumentException("Publication date cannot be null");
        }
    }
    
    private Book createBook(DocumentCreationParams params) throws InvalidDocumentException {
        String isbn = params.getExtraParam("isbn");
        String pagesStr = params.getExtraParam("pages");
        String genre = params.getExtraParam("genre");
        
        if (!InputValidator.isValidString(isbn)) {
            throw new InvalidDocumentException("Invalid ISBN");
        }
        
        int pages;
        try {
            pages = Integer.parseInt(pagesStr);
            if (pages <= 0) {
                throw new InvalidDocumentException("Pages must be positive");
            }
        } catch (NumberFormatException e) {
            throw new InvalidDocumentException("Invalid page number format");
        }
        
        if (!InputValidator.isValidString(genre)) {
            throw new InvalidDocumentException("Invalid genre");
        }
        
        return new Book(
            params.getId(),
            params.getTitle(),
            params.getAuthor(),
            params.getPublicationDate(),
            isbn,
            pages,
            genre
        );
    }
    
    private Magazine createMagazine(DocumentCreationParams params) throws InvalidDocumentException {
        String issueNumberStr = params.getExtraParam("issueNumber");
        String publisher = params.getExtraParam("publisher");
        String frequency = params.getExtraParam("frequency");
        
        int issueNumber;
        try {
            issueNumber = Integer.parseInt(issueNumberStr);
            if (issueNumber <= 0) {
                throw new InvalidDocumentException("Issue number must be positive");
            }
        } catch (NumberFormatException e) {
            throw new InvalidDocumentException("Invalid issue number format");
        }
        
        if (!InputValidator.isValidString(publisher)) {
            throw new InvalidDocumentException("Invalid publisher");
        }
        
        if (!InputValidator.isValidString(frequency)) {
            throw new InvalidDocumentException("Invalid frequency");
        }
        
        return new Magazine(
            params.getId(),
            params.getTitle(),
            params.getAuthor(),
            params.getPublicationDate(),
            issueNumber,
            publisher,
            frequency
        );
    }
}
