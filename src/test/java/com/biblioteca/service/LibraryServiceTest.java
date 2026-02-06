package com.biblioteca.service;

import com.biblioteca.exception.*;
import com.biblioteca.factory.DocumentCreationParams;
import com.biblioteca.io.DataPersistence;
import com.biblioteca.io.FileManager;
import com.biblioteca.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LibraryService
 */
public class LibraryServiceTest {
    
    @TempDir
    Path tempDir;
    
    private LibraryService libraryService;
    private User testUser;
    
    @BeforeEach
    public void setUp() {
        FileManager fileManager = new FileManager(tempDir.toString());
        DataPersistence dataPersistence = new DataPersistence(fileManager);
        libraryService = new LibraryService(dataPersistence);
        
        // Create test user
        testUser = new User("U001", "Test User", "test@example.com", 
                           "1234567890", User.UserType.STUDENT);
        try {
            libraryService.registerUser(testUser);
        } catch (LibraryException e) {
            fail("Failed to register test user: " + e.getMessage());
        }
    }
    
    @Test
    public void testAddValidBook() throws LibraryException {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("B001")
            .title("Test Book")
            .author("Test Author")
            .publicationDate(LocalDate.of(2020, 1, 1))
            .addExtraParam("isbn", "978-0-123456-78-9")
            .addExtraParam("pages", "300")
            .addExtraParam("genre", "Fiction")
            .build();
        
        libraryService.addDocument(Document.DocumentType.BOOK, params);
        
        Document doc = libraryService.findDocumentById("B001");
        assertNotNull(doc);
        assertEquals("Test Book", doc.getTitle());
    }
    
    @Test
    public void testAddDuplicateDocument() throws LibraryException {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id("B001")
            .title("Test Book")
            .author("Test Author")
            .publicationDate(LocalDate.now())
            .addExtraParam("isbn", "978-0-123456-78-9")
            .addExtraParam("pages", "300")
            .addExtraParam("genre", "Fiction")
            .build();
        
        libraryService.addDocument(Document.DocumentType.BOOK, params);
        
        assertThrows(LibraryException.class, () -> {
            libraryService.addDocument(Document.DocumentType.BOOK, params);
        });
    }
    
    @Test
    public void testFindNonExistentDocument() {
        Document doc = libraryService.findDocumentById("NONEXISTENT");
        assertNull(doc);
    }
    
    @Test
    public void testGetAllDocuments() throws LibraryException {
        assertEquals(0, libraryService.getAllDocuments().size());
        
        addTestBook("B001");
        addTestBook("B002");
        
        assertEquals(2, libraryService.getAllDocuments().size());
    }
    
    @Test
    public void testRegisterUser() throws LibraryException {
        User newUser = new User("U002", "New User", "new@example.com",
                               "9876543210", User.UserType.TEACHER);
        
        libraryService.registerUser(newUser);
        
        User found = libraryService.findUserById("U002");
        assertNotNull(found);
        assertEquals("New User", found.getName());
    }
    
    @Test
    public void testRegisterDuplicateUser() {
        assertThrows(LibraryException.class, () -> {
            libraryService.registerUser(testUser);
        });
    }
    
    @Test
    public void testCreateLoan() throws LibraryException {
        addTestBook("B001");
        
        Loan loan = libraryService.createLoan("U001", "B001");
        
        assertNotNull(loan);
        assertEquals("U001", loan.getUserId());
        assertEquals("B001", loan.getDocumentId());
        assertEquals(Loan.LoanStatus.ACTIVE, loan.getStatus());
        
        // Document should no longer be available
        Document doc = libraryService.findDocumentById("B001");
        assertFalse(doc.isAvailable());
    }
    
    @Test
    public void testCreateLoanForUnavailableDocument() throws LibraryException {
        addTestBook("B001");
        libraryService.createLoan("U001", "B001");
        
        // Try to create another loan for the same document
        assertThrows(LibraryException.class, () -> {
            libraryService.createLoan("U001", "B001");
        });
    }
    
    @Test
    public void testReturnDocument() throws LibraryException {
        addTestBook("B001");
        Loan loan = libraryService.createLoan("U001", "B001");
        
        libraryService.returnDocument(loan.getLoanId());
        
        // Document should be available again
        Document doc = libraryService.findDocumentById("B001");
        assertTrue(doc.isAvailable());
        
        // Loan status should be RETURNED
        List<Loan> activeLoans = libraryService.getActiveLoans();
        assertFalse(activeLoans.contains(loan));
    }
    
    @Test
    public void testReturnNonExistentLoan() {
        assertThrows(LibraryException.class, () -> {
            libraryService.returnDocument("NONEXISTENT");
        });
    }
    
    @Test
    public void testGetStatistics() throws LibraryException {
        addTestBook("B001");
        addTestBook("B002");
        
        User user2 = new User("U002", "User Two", "user2@example.com",
                             "1111111111", User.UserType.STUDENT);
        libraryService.registerUser(user2);
        
        libraryService.createLoan("U001", "B001");
        
        Map<String, Integer> stats = libraryService.getStatistics();
        
        assertEquals(2, stats.get("totalDocuments"));
        assertEquals(1, stats.get("availableDocuments"));
        assertEquals(2, stats.get("totalUsers"));
        assertEquals(1, stats.get("activeLoans"));
    }
    
    @Test
    public void testMaxLoanLimit() throws LibraryException {
        // Student can borrow max 5 books
        for (int i = 1; i <= 5; i++) {
            addTestBook("B" + String.format("%03d", i));
            libraryService.createLoan("U001", "B" + String.format("%03d", i));
        }
        
        // Try to borrow 6th book
        addTestBook("B006");
        assertThrows(LibraryException.class, () -> {
            libraryService.createLoan("U001", "B006");
        });
    }
    
    @Test
    public void testRemoveDocumentOnLoan() throws LibraryException {
        addTestBook("B001");
        libraryService.createLoan("U001", "B001");
        
        assertThrows(LibraryException.class, () -> {
            libraryService.removeDocument("B001");
        });
    }
    
    // Helper method
    private void addTestBook(String id) throws LibraryException {
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id(id)
            .title("Book " + id)
            .author("Author " + id)
            .publicationDate(LocalDate.now())
            .addExtraParam("isbn", "ISBN-" + id)
            .addExtraParam("pages", "200")
            .addExtraParam("genre", "Fiction")
            .build();
        
        libraryService.addDocument(Document.DocumentType.BOOK, params);
    }
}
