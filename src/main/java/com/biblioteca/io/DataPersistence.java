package com.biblioteca.io;

import com.biblioteca.exception.LibraryException;
import com.biblioteca.model.*;
import com.biblioteca.util.LibraryLogger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles data persistence for library entities.
 * Manages saving and loading documents, users, and loans.
 */
public class DataPersistence {
    
    private static final String DOCUMENTS_FILE = "documents.txt";
    private static final String USERS_FILE = "users.txt";
    private static final String LOANS_FILE = "loans.txt";
    private static final String DELIMITER = "|";
    
    private final FileManager fileManager;
    private final LibraryLogger logger;
    
    public DataPersistence(FileManager fileManager) {
        this.fileManager = fileManager;
        this.logger = LibraryLogger.getInstance();
    }
    
    // ==================== DOCUMENTS ====================
    
    /**
     * Save documents to file
     */
    public void saveDocuments(List<Document> documents) throws LibraryException {
        List<String> lines = new ArrayList<>();
        
        for (Document doc : documents) {
            String line = serializeDocument(doc);
            lines.add(line);
        }
        
        fileManager.writeTextFile(DOCUMENTS_FILE, lines);
        logger.info("Saved " + documents.size() + " documents");
    }
    
    /**
     * Load documents from file
     */
    public List<Document> loadDocuments() throws LibraryException {
        List<String> lines = fileManager.readTextFile(DOCUMENTS_FILE);
        List<Document> documents = new ArrayList<>();
        
        for (String line : lines) {
            try {
                Document doc = deserializeDocument(line);
                if (doc != null) {
                    documents.add(doc);
                }
            } catch (Exception e) {
                logger.warning("Failed to deserialize document: " + line);
            }
        }
        
        logger.info("Loaded " + documents.size() + " documents");
        return documents;
    }
    
    private String serializeDocument(Document doc) {
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getType()).append(DELIMITER);
        sb.append(doc.getId()).append(DELIMITER);
        sb.append(doc.getTitle()).append(DELIMITER);
        sb.append(doc.getAuthor()).append(DELIMITER);
        sb.append(doc.getPublicationDate()).append(DELIMITER);
        sb.append(doc.isAvailable()).append(DELIMITER);
        
        if (doc instanceof Book) {
            Book book = (Book) doc;
            sb.append(book.getIsbn()).append(DELIMITER);
            sb.append(book.getPages()).append(DELIMITER);
            sb.append(book.getGenre());
        } else if (doc instanceof Magazine) {
            Magazine mag = (Magazine) doc;
            sb.append(mag.getIssueNumber()).append(DELIMITER);
            sb.append(mag.getPublisher()).append(DELIMITER);
            sb.append(mag.getFrequency());
        }
        
        return sb.toString();
    }
    
    private Document deserializeDocument(String line) {
        String[] parts = line.split("\\" + DELIMITER);
        if (parts.length < 6) {
            return null;
        }
        
        Document.DocumentType type = Document.DocumentType.valueOf(parts[0]);
        String id = parts[1];
        String title = parts[2];
        String author = parts[3];
        LocalDate pubDate = LocalDate.parse(parts[4]);
        boolean available = Boolean.parseBoolean(parts[5]);
        
        Document doc = null;
        
        if (type == Document.DocumentType.BOOK && parts.length >= 9) {
            doc = new Book(id, title, author, pubDate, 
                          parts[6], Integer.parseInt(parts[7]), parts[8]);
        } else if (type == Document.DocumentType.MAGAZINE && parts.length >= 9) {
            doc = new Magazine(id, title, author, pubDate,
                             Integer.parseInt(parts[6]), parts[7], parts[8]);
        }
        
        if (doc != null) {
            doc.setAvailable(available);
        }
        
        return doc;
    }
    
    // ==================== USERS ====================
    
    /**
     * Save users to file
     */
    public void saveUsers(List<User> users) throws LibraryException {
        List<String> lines = new ArrayList<>();
        
        for (User user : users) {
            String line = serializeUser(user);
            lines.add(line);
        }
        
        fileManager.writeTextFile(USERS_FILE, lines);
        logger.info("Saved " + users.size() + " users");
    }
    
    /**
     * Load users from file
     */
    public List<User> loadUsers() throws LibraryException {
        List<String> lines = fileManager.readTextFile(USERS_FILE);
        List<User> users = new ArrayList<>();
        
        for (String line : lines) {
            try {
                User user = deserializeUser(line);
                if (user != null) {
                    users.add(user);
                }
            } catch (Exception e) {
                logger.warning("Failed to deserialize user: " + line);
            }
        }
        
        logger.info("Loaded " + users.size() + " users");
        return users;
    }
    
    private String serializeUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getUserId()).append(DELIMITER);
        sb.append(user.getName()).append(DELIMITER);
        sb.append(user.getEmail()).append(DELIMITER);
        sb.append(user.getPhone()).append(DELIMITER);
        sb.append(user.getRegistrationDate()).append(DELIMITER);
        sb.append(user.getUserType()).append(DELIMITER);
        sb.append(String.join(",", user.getCurrentLoans()));
        return sb.toString();
    }
    
    private User deserializeUser(String line) {
        String[] parts = line.split("\\" + DELIMITER);
        if (parts.length < 6) {
            return null;
        }
        
        User user = new User(parts[0], parts[1], parts[2], parts[3], 
                            User.UserType.valueOf(parts[5]));
        user.setRegistrationDate(LocalDate.parse(parts[4]));
        
        if (parts.length >= 7 && !parts[6].isEmpty()) {
            String[] loans = parts[6].split(",");
            for (String loanId : loans) {
                user.addLoan(loanId);
            }
        }
        
        return user;
    }
    
    // ==================== LOANS ====================
    
    /**
     * Save loans to file
     */
    public void saveLoans(List<Loan> loans) throws LibraryException {
        List<String> lines = new ArrayList<>();
        
        for (Loan loan : loans) {
            String line = serializeLoan(loan);
            lines.add(line);
        }
        
        fileManager.writeTextFile(LOANS_FILE, lines);
        logger.info("Saved " + loans.size() + " loans");
    }
    
    /**
     * Load loans from file
     */
    public List<Loan> loadLoans() throws LibraryException {
        List<String> lines = fileManager.readTextFile(LOANS_FILE);
        List<Loan> loans = new ArrayList<>();
        
        for (String line : lines) {
            try {
                Loan loan = deserializeLoan(line);
                if (loan != null) {
                    loans.add(loan);
                }
            } catch (Exception e) {
                logger.warning("Failed to deserialize loan: " + line);
            }
        }
        
        logger.info("Loaded " + loans.size() + " loans");
        return loans;
    }
    
    private String serializeLoan(Loan loan) {
        StringBuilder sb = new StringBuilder();
        sb.append(loan.getLoanId()).append(DELIMITER);
        sb.append(loan.getUserId()).append(DELIMITER);
        sb.append(loan.getDocumentId()).append(DELIMITER);
        sb.append(loan.getLoanDate()).append(DELIMITER);
        sb.append(loan.getDueDate()).append(DELIMITER);
        sb.append(loan.getReturnDate() != null ? loan.getReturnDate() : "").append(DELIMITER);
        sb.append(loan.getStatus());
        return sb.toString();
    }
    
    private Loan deserializeLoan(String line) {
        String[] parts = line.split("\\" + DELIMITER);
        if (parts.length < 7) {
            return null;
        }
        
        Loan loan = new Loan(parts[0], parts[1], parts[2],
                            LocalDate.parse(parts[3]), LocalDate.parse(parts[4]));
        
        if (!parts[5].isEmpty()) {
            loan.setReturnDate(LocalDate.parse(parts[5]));
        }
        
        loan.setStatus(Loan.LoanStatus.valueOf(parts[6]));
        
        return loan;
    }
}
