package com.biblioteca.service;

import com.biblioteca.exception.*;
import com.biblioteca.factory.DocumentCreationParams;
import com.biblioteca.factory.DocumentFactory;
import com.biblioteca.io.DataPersistence;
import com.biblioteca.iterator.DocumentCollection;
import com.biblioteca.iterator.Iterator;
import com.biblioteca.model.*;
import com.biblioteca.observer.LoanNotificationSystem;
import com.biblioteca.strategy.SearchContext;
import com.biblioteca.strategy.SearchStrategy;
import com.biblioteca.util.LibraryLogger;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main service class for library operations.
 * Coordinates between different components and implements business logic.
 */
public class LibraryService {
    
    private final DocumentCollection documentCollection;
    private final Map<String, User> users;
    private final Map<String, Loan> loans;
    private final DocumentFactory documentFactory;
    private final SearchContext searchContext;
    private final LoanNotificationSystem notificationSystem;
    private final DataPersistence dataPersistence;
    private final LibraryLogger logger;
    
    private int loanIdCounter;
    
    public LibraryService(DataPersistence dataPersistence) {
        this.documentCollection = new DocumentCollection();
        this.users = new HashMap<>();
        this.loans = new HashMap<>();
        this.documentFactory = DocumentFactory.getInstance();
        this.searchContext = new SearchContext();
        this.notificationSystem = new LoanNotificationSystem();
        this.dataPersistence = dataPersistence;
        this.logger = LibraryLogger.getInstance();
        this.loanIdCounter = 1;
        
        loadData();
    }
    
    // ==================== DOCUMENT OPERATIONS ====================
    
    /**
     * Add a new document to the library
     */
    public void addDocument(Document.DocumentType type, DocumentCreationParams params) 
            throws LibraryException {
        try {
            Document document = documentFactory.createDocument(type, params);
            
            // Check if document ID already exists
            if (findDocumentById(document.getId()) != null) {
                throw new LibraryException("Document with ID " + document.getId() + " already exists");
            }
            
            documentCollection.add(document);
            logger.info("Added document: " + document.getId());
            
            saveData();
        } catch (Exception e) {
            logger.error("Failed to add document", e);
            throw ExceptionHandler.wrapException(e);
        }
    }
    
    /**
     * Find document by ID
     */
    public Document findDocumentById(String id) {
        Iterator<Document> iterator = documentCollection.createIterator();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            if (doc.getId().equals(id)) {
                return doc;
            }
        }
        return null;
    }
    
    /**
     * Search documents using current search strategy
     */
    public List<Document> searchDocuments(String query) {
        return searchContext.executeSearch(documentCollection.getAll(), query);
    }
    
    /**
     * Search documents with specific strategy
     */
    public List<Document> searchDocuments(String query, SearchStrategy strategy) {
        searchContext.setStrategy(strategy);
        return searchContext.executeSearch(documentCollection.getAll(), query);
    }
    
    /**
     * Get all documents
     */
    public List<Document> getAllDocuments() {
        return documentCollection.getAll();
    }
    
    /**
     * Get available documents
     */
    public List<Document> getAvailableDocuments() {
        return documentCollection.getAll().stream()
            .filter(Document::isAvailable)
            .collect(Collectors.toList());
    }
    
    /**
     * Remove document by ID
     */
    public boolean removeDocument(String documentId) throws LibraryException {
        Document doc = findDocumentById(documentId);
        if (doc == null) {
            throw new DocumentNotFoundException(documentId);
        }
        
        if (!doc.isAvailable()) {
            throw new LibraryException("Cannot remove document that is currently on loan");
        }
        
        documentCollection.remove(doc);
        logger.info("Removed document: " + documentId);
        saveData();
        return true;
    }
    
    // ==================== USER OPERATIONS ====================
    
    /**
     * Register a new user
     */
    public void registerUser(User user) throws LibraryException {
        if (users.containsKey(user.getUserId())) {
            throw new LibraryException("User with ID " + user.getUserId() + " already exists");
        }
        
        users.put(user.getUserId(), user);
        logger.info("Registered user: " + user.getUserId());
        saveData();
    }
    
    /**
     * Find user by ID
     */
    public User findUserById(String userId) throws LibraryException {
        User user = users.get(userId);
        if (user == null) {
            throw new LibraryException("User not found: " + userId);
        }
        return user;
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // ==================== LOAN OPERATIONS ====================
    
    /**
     * Create a new loan
     */
    public Loan createLoan(String userId, String documentId) throws LibraryException {
        try {
            // Validate user
            User user = findUserById(userId);
            if (!user.canBorrow()) {
                throw new LibraryException("User has reached maximum loan limit");
            }
            
            // Validate document
            Document document = findDocumentById(documentId);
            if (document == null) {
                throw new DocumentNotFoundException(documentId);
            }
            
            if (!document.isAvailable()) {
                throw new LibraryException("Document is not available for loan");
            }
            
            // Create loan
            String loanId = "L" + String.format("%04d", loanIdCounter++);
            LocalDate loanDate = LocalDate.now();
            LocalDate dueDate = loanDate.plusDays(14); // 2 weeks loan period
            
            Loan loan = new Loan(loanId, userId, documentId, loanDate, dueDate);
            
            // Update states
            document.setAvailable(false);
            user.addLoan(documentId);
            loans.put(loanId, loan);
            
            // Notify observers
            notificationSystem.notifyLoanCreated(userId, documentId);
            
            logger.info("Created loan: " + loanId);
            saveData();
            
            return loan;
            
        } catch (LibraryException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to create loan", e);
            throw new LibraryException("Failed to create loan", e);
        }
    }
    
    /**
     * Return a document
     */
    public void returnDocument(String loanId) throws LibraryException {
        try {
            Loan loan = loans.get(loanId);
            if (loan == null) {
                throw new LibraryException("Loan not found: " + loanId);
            }
            
            if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
                throw new LibraryException("Document already returned");
            }
            
            // Update states
            Document document = findDocumentById(loan.getDocumentId());
            User user = findUserById(loan.getUserId());
            
            loan.returnDocument();
            document.setAvailable(true);
            user.removeLoan(loan.getDocumentId());
            
            // Notify observers
            notificationSystem.notifyLoanReturned(user.getUserId(), document.getId());
            
            logger.info("Returned loan: " + loanId);
            saveData();
            
        } catch (LibraryException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to return document", e);
            throw new LibraryException("Failed to return document", e);
        }
    }
    
    /**
     * Get all active loans
     */
    public List<Loan> getActiveLoans() {
        return loans.values().stream()
            .filter(loan -> loan.getStatus() == Loan.LoanStatus.ACTIVE)
            .collect(Collectors.toList());
    }
    
    /**
     * Get overdue loans
     */
    public List<Loan> getOverdueLoans() {
        return loans.values().stream()
            .filter(Loan::isOverdue)
            .collect(Collectors.toList());
    }
    
    /**
     * Get loans for a specific user
     */
    public List<Loan> getUserLoans(String userId) {
        return loans.values().stream()
            .filter(loan -> loan.getUserId().equals(userId))
            .collect(Collectors.toList());
    }
    
    // ==================== NOTIFICATION SYSTEM ====================
    
    public LoanNotificationSystem getNotificationSystem() {
        return notificationSystem;
    }
    
    // ==================== DATA PERSISTENCE ====================
    
    /**
     * Load data from files
     */
    private void loadData() {
        try {
            // Load documents
            List<Document> docs = dataPersistence.loadDocuments();
            for (Document doc : docs) {
                documentCollection.add(doc);
            }
            
            // Load users
            List<User> userList = dataPersistence.loadUsers();
            for (User user : userList) {
                users.put(user.getUserId(), user);
            }
            
            // Load loans
            List<Loan> loanList = dataPersistence.loadLoans();
            for (Loan loan : loanList) {
                loans.put(loan.getLoanId(), loan);
                // Update counter
                String idNum = loan.getLoanId().substring(1);
                int num = Integer.parseInt(idNum);
                if (num >= loanIdCounter) {
                    loanIdCounter = num + 1;
                }
            }
            
            logger.info("Data loaded successfully");
        } catch (Exception e) {
            logger.warning("Failed to load data: " + e.getMessage());
        }
    }
    
    /**
     * Save data to files
     */
    private void saveData() {
        try {
            dataPersistence.saveDocuments(documentCollection.getAll());
            dataPersistence.saveUsers(new ArrayList<>(users.values()));
            dataPersistence.saveLoans(new ArrayList<>(loans.values()));
            logger.debug("Data saved successfully");
        } catch (Exception e) {
            logger.error("Failed to save data", e);
        }
    }
    
    /**
     * Get statistics
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalDocuments", documentCollection.size());
        stats.put("availableDocuments", (int) documentCollection.getAll().stream()
            .filter(Document::isAvailable).count());
        stats.put("totalUsers", users.size());
        stats.put("activeLoans", (int) loans.values().stream()
            .filter(l -> l.getStatus() == Loan.LoanStatus.ACTIVE).count());
        stats.put("overdueLoans", (int) loans.values().stream()
            .filter(Loan::isOverdue).count());
        return stats;
    }
}
