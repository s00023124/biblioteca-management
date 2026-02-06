package com.biblioteca.exception;

/**
 * Exception thrown when a document is not found.
 */
public class DocumentNotFoundException extends LibraryException {
    public DocumentNotFoundException(String documentId) {
        super("Document not found: " + documentId, "DOC_NOT_FOUND");
    }
}
