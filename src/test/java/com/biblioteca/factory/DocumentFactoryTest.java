package com.biblioteca.factory;

import com.biblioteca.exception.InvalidDocumentException;
import com.biblioteca.model.Book;
import com.biblioteca.model.Document;
import com.biblioteca.model.Magazine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DocumentFactory
 */
public class DocumentFactoryTest {
    
    private DocumentFactory factory;
    
    @BeforeEach
    public void setUp() {
        factory = DocumentFactory.getInstance();
    }
    
    @Test
    public void testSingletonInstance() {
        DocumentFactory instance1 = DocumentFactory.getInstance();
        DocumentFactory instance2 = DocumentFactory.getInstance();
        assertSame(instance1, instance2, "Factory should return same instance");
    }
    
    @Test
    public void testCreateValidBook() throws InvalidDocumentException {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("B001")
            .title("Test Book")
            .author("Test Author")
            .publicationDate(LocalDate.of(2020, 1, 1))
            .addExtraParam("isbn", "978-0-123456-78-9")
            .addExtraParam("pages", "300")
            .addExtraParam("genre", "Fiction")
            .build();
        
        Document doc = factory.createDocument(Document.DocumentType.BOOK, params);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Book);
        assertEquals("B001", doc.getId());
        assertEquals("Test Book", doc.getTitle());
        assertEquals("Test Author", doc.getAuthor());
        assertTrue(doc.isAvailable());
    }
    
    @Test
    public void testCreateValidMagazine() throws InvalidDocumentException {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("M001")
            .title("Tech Magazine")
            .author("Editorial Team")
            .publicationDate(LocalDate.of(2023, 6, 1))
            .addExtraParam("issueNumber", "42")
            .addExtraParam("publisher", "Tech Publications")
            .addExtraParam("frequency", "Monthly")
            .build();
        
        Document doc = factory.createDocument(Document.DocumentType.MAGAZINE, params);
        
        assertNotNull(doc);
        assertTrue(doc instanceof Magazine);
        assertEquals("M001", doc.getId());
        assertEquals("Tech Magazine", doc.getTitle());
    }
    
    @Test
    public void testCreateBookWithInvalidPages() {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("B002")
            .title("Invalid Book")
            .author("Author")
            .publicationDate(LocalDate.now())
            .addExtraParam("isbn", "978-0-123456-78-9")
            .addExtraParam("pages", "-10")
            .addExtraParam("genre", "Fiction")
            .build();
        
        assertThrows(InvalidDocumentException.class, () -> {
            factory.createDocument(Document.DocumentType.BOOK, params);
        });
    }
    
    @Test
    public void testCreateBookWithInvalidISBN() {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("B003")
            .title("Book")
            .author("Author")
            .publicationDate(LocalDate.now())
            .addExtraParam("isbn", "")
            .addExtraParam("pages", "200")
            .addExtraParam("genre", "Fiction")
            .build();
        
        assertThrows(InvalidDocumentException.class, () -> {
            factory.createDocument(Document.DocumentType.BOOK, params);
        });
    }
    
    @Test
    public void testCreateDocumentWithNullParams() {
        assertThrows(InvalidDocumentException.class, () -> {
            factory.createDocument(Document.DocumentType.BOOK, null);
        });
    }
    
    @Test
    public void testCreateDocumentWithInvalidId() {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("")
            .title("Book")
            .author("Author")
            .publicationDate(LocalDate.now())
            .addExtraParam("isbn", "978-0-123456-78-9")
            .addExtraParam("pages", "200")
            .addExtraParam("genre", "Fiction")
            .build();
        
        assertThrows(InvalidDocumentException.class, () -> {
            factory.createDocument(Document.DocumentType.BOOK, params);
        });
    }
}
