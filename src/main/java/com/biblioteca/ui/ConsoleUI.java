package com.biblioteca.ui;

import com.biblioteca.exception.*;
import com.biblioteca.factory.DocumentCreationParams;
import com.biblioteca.model.*;
import com.biblioteca.observer.ConsoleNotifier;
import com.biblioteca.service.LibraryService;
import com.biblioteca.strategy.*;
import com.biblioteca.util.InputValidator;
import com.biblioteca.util.LibraryLogger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console-based user interface for the library system.
 * Handles all user interactions and menu navigation.
 */
public class ConsoleUI {
    
    private final LibraryService libraryService;
    private final Scanner scanner;
    private final LibraryLogger logger;
    private final DateTimeFormatter dateFormatter;
    
    public ConsoleUI(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.scanner = new Scanner(System.in);
        this.logger = LibraryLogger.getInstance();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Register console notifier
        libraryService.getNotificationSystem().attach(new ConsoleNotifier("System"));
    }
    
    /**
     * Start the console interface
     */
    public void start() {
        logger.info("Starting Library Management System");
        System.out.println("\n=================================");
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.println("=================================\n");
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = readIntInput("Enter choice: ");
                running = handleMainMenu(choice);
            } catch (Exception e) {
                String userMessage = ExceptionHandler.handleException(e);
                System.out.println("\nERROR: " + userMessage + "\n");
            }
        }
        
        System.out.println("\nThank you for using Library Management System!");
        scanner.close();
    }
    
    private void displayMainMenu() {
        System.out.println("=== MAIN MENU ===");
        System.out.println("1. Document Management");
        System.out.println("2. User Management");
        System.out.println("3. Loan Management");
        System.out.println("4. Search Documents");
        System.out.println("5. View Statistics");
        System.out.println("0. Exit");
        System.out.println();
    }
    
    private boolean handleMainMenu(int choice) {
        switch (choice) {
            case 1:
                documentManagementMenu();
                break;
            case 2:
                userManagementMenu();
                break;
            case 3:
                loanManagementMenu();
                break;
            case 4:
                searchDocumentsMenu();
                break;
            case 5:
                displayStatistics();
                break;
            case 0:
                return false;
            default:
                System.out.println("Invalid choice. Please try again.\n");
        }
        return true;
    }
    
    // ==================== DOCUMENT MANAGEMENT ====================
    
    private void documentManagementMenu() {
        System.out.println("\n=== DOCUMENT MANAGEMENT ===");
        System.out.println("1. Add Book");
        System.out.println("2. Add Magazine");
        System.out.println("3. List All Documents");
        System.out.println("4. List Available Documents");
        System.out.println("5. View Document Details");
        System.out.println("6. Remove Document");
        System.out.println("0. Back to Main Menu");
        System.out.println();
        
        int choice = readIntInput("Enter choice: ");
        
        try {
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    addMagazine();
                    break;
                case 3:
                    listAllDocuments();
                    break;
                case 4:
                    listAvailableDocuments();
                    break;
                case 5:
                    viewDocumentDetails();
                    break;
                case 6:
                    removeDocument();
                    break;
            }
        } catch (Exception e) {
            String userMessage = ExceptionHandler.handleException(e);
            System.out.println("\nERROR: " + userMessage + "\n");
        }
    }
    
    private void addBook() throws LibraryException {
        System.out.println("\n--- Add New Book ---");
        
        String id = readInput("Document ID: ");
        String title = readInput("Title: ");
        String author = readInput("Author: ");
        String pubDateStr = readInput("Publication Date (yyyy-MM-dd): ");
        String isbn = readInput("ISBN: ");
        int pages = readIntInput("Number of Pages: ");
        String genre = readInput("Genre: ");
        
        LocalDate pubDate = parseDate(pubDateStr);
        
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id(id)
            .title(title)
            .author(author)
            .publicationDate(pubDate)
            .addExtraParam("isbn", isbn)
            .addExtraParam("pages", String.valueOf(pages))
            .addExtraParam("genre", genre)
            .build();
        
        libraryService.addDocument(Document.DocumentType.BOOK, params);
        System.out.println("\n✓ Book added successfully!\n");
    }
    
    private void addMagazine() throws LibraryException {
        System.out.println("\n--- Add New Magazine ---");
        
        String id = readInput("Document ID: ");
        String title = readInput("Title: ");
        String author = readInput("Editor/Author: ");
        String pubDateStr = readInput("Publication Date (yyyy-MM-dd): ");
        int issueNumber = readIntInput("Issue Number: ");
        String publisher = readInput("Publisher: ");
        String frequency = readInput("Frequency (e.g., Monthly, Weekly): ");
        
        LocalDate pubDate = parseDate(pubDateStr);
        
        DocumentCreationParams params = new DocumentCreationParams.Builder()
            .id(id)
            .title(title)
            .author(author)
            .publicationDate(pubDate)
            .addExtraParam("issueNumber", String.valueOf(issueNumber))
            .addExtraParam("publisher", publisher)
            .addExtraParam("frequency", frequency)
            .build();
        
        libraryService.addDocument(Document.DocumentType.MAGAZINE, params);
        System.out.println("\n✓ Magazine added successfully!\n");
    }
    
    private void listAllDocuments() {
        System.out.println("\n=== ALL DOCUMENTS ===");
        List<Document> documents = libraryService.getAllDocuments();
        
        if (documents.isEmpty()) {
            System.out.println("No documents in the library.");
        } else {
            for (Document doc : documents) {
                System.out.println(doc.toString());
            }
        }
        System.out.println();
    }
    
    private void listAvailableDocuments() {
        System.out.println("\n=== AVAILABLE DOCUMENTS ===");
        List<Document> documents = libraryService.getAvailableDocuments();
        
        if (documents.isEmpty()) {
            System.out.println("No documents available.");
        } else {
            for (Document doc : documents) {
                System.out.println(doc.toString());
            }
        }
        System.out.println();
    }
    
    private void viewDocumentDetails() {
        String id = readInput("\nEnter Document ID: ");
        Document doc = libraryService.findDocumentById(id);
        
        if (doc == null) {
            System.out.println("Document not found.\n");
        } else {
            System.out.println("\n" + doc.getDisplayInfo());
        }
    }
    
    private void removeDocument() throws LibraryException {
        String id = readInput("\nEnter Document ID to remove: ");
        libraryService.removeDocument(id);
        System.out.println("\n✓ Document removed successfully!\n");
    }
    
    // ==================== USER MANAGEMENT ====================
    
    private void userManagementMenu() {
        System.out.println("\n=== USER MANAGEMENT ===");
        System.out.println("1. Register New User");
        System.out.println("2. List All Users");
        System.out.println("3. View User Details");
        System.out.println("0. Back to Main Menu");
        System.out.println();
        
        int choice = readIntInput("Enter choice: ");
        
        try {
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    listAllUsers();
                    break;
                case 3:
                    viewUserDetails();
                    break;
            }
        } catch (Exception e) {
            String userMessage = ExceptionHandler.handleException(e);
            System.out.println("\nERROR: " + userMessage + "\n");
        }
    }
    
    private void registerUser() throws LibraryException {
        System.out.println("\n--- Register New User ---");
        
        String userId = readInput("User ID: ");
        String name = readInput("Name: ");
        String email = readInput("Email: ");
        String phone = readInput("Phone: ");
        
        System.out.println("User Type:");
        System.out.println("1. Student (max 5 loans)");
        System.out.println("2. Teacher (max 10 loans)");
        System.out.println("3. External (max 3 loans)");
        int typeChoice = readIntInput("Enter choice: ");
        
        User.UserType userType;
        switch (typeChoice) {
            case 1:
                userType = User.UserType.STUDENT;
                break;
            case 2:
                userType = User.UserType.TEACHER;
                break;
            case 3:
                userType = User.UserType.EXTERNAL;
                break;
            default:
                throw new LibraryException("Invalid user type");
        }
        
        User user = new User(userId, name, email, phone, userType);
        libraryService.registerUser(user);
        System.out.println("\n✓ User registered successfully!\n");
    }
    
    private void listAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        List<User> users = libraryService.getAllUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users registered.");
        } else {
            for (User user : users) {
                System.out.println(user.toString());
            }
        }
        System.out.println();
    }
    
    private void viewUserDetails() throws LibraryException {
        String userId = readInput("\nEnter User ID: ");
        User user = libraryService.findUserById(userId);
        
        System.out.println("\n=== USER DETAILS ===");
        System.out.println("ID: " + user.getUserId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Type: " + user.getUserType());
        System.out.println("Registration Date: " + user.getRegistrationDate());
        System.out.println("Current Loans: " + user.getCurrentLoans().size() + 
                         "/" + user.getUserType().getMaxLoans());
        System.out.println();
    }
    
    // ==================== LOAN MANAGEMENT ====================
    
    private void loanManagementMenu() {
        System.out.println("\n=== LOAN MANAGEMENT ===");
        System.out.println("1. Create New Loan");
        System.out.println("2. Return Document");
        System.out.println("3. View Active Loans");
        System.out.println("4. View Overdue Loans");
        System.out.println("5. View User Loans");
        System.out.println("0. Back to Main Menu");
        System.out.println();
        
        int choice = readIntInput("Enter choice: ");
        
        try {
            switch (choice) {
                case 1:
                    createLoan();
                    break;
                case 2:
                    returnDocument();
                    break;
                case 3:
                    viewActiveLoans();
                    break;
                case 4:
                    viewOverdueLoans();
                    break;
                case 5:
                    viewUserLoans();
                    break;
            }
        } catch (Exception e) {
            String userMessage = ExceptionHandler.handleException(e);
            System.out.println("\nERROR: " + userMessage + "\n");
        }
    }
    
    private void createLoan() throws LibraryException {
        System.out.println("\n--- Create New Loan ---");
        
        String userId = readInput("User ID: ");
        String documentId = readInput("Document ID: ");
        
        Loan loan = libraryService.createLoan(userId, documentId);
        System.out.println("\n✓ Loan created successfully!");
        System.out.println("Loan ID: " + loan.getLoanId());
        System.out.println("Due Date: " + loan.getDueDate());
        System.out.println();
    }
    
    private void returnDocument() throws LibraryException {
        String loanId = readInput("\nEnter Loan ID: ");
        libraryService.returnDocument(loanId);
        System.out.println("\n✓ Document returned successfully!\n");
    }
    
    private void viewActiveLoans() {
        System.out.println("\n=== ACTIVE LOANS ===");
        List<Loan> loans = libraryService.getActiveLoans();
        
        if (loans.isEmpty()) {
            System.out.println("No active loans.");
        } else {
            for (Loan loan : loans) {
                System.out.println(loan.toString() + " - Due: " + loan.getDueDate());
            }
        }
        System.out.println();
    }
    
    private void viewOverdueLoans() {
        System.out.println("\n=== OVERDUE LOANS ===");
        List<Loan> loans = libraryService.getOverdueLoans();
        
        if (loans.isEmpty()) {
            System.out.println("No overdue loans.");
        } else {
            for (Loan loan : loans) {
                System.out.println(loan.toString() + " - Due: " + loan.getDueDate());
            }
        }
        System.out.println();
    }
    
    private void viewUserLoans() throws LibraryException {
        String userId = readInput("\nEnter User ID: ");
        List<Loan> loans = libraryService.getUserLoans(userId);
        
        System.out.println("\n=== LOANS FOR USER " + userId + " ===");
        if (loans.isEmpty()) {
            System.out.println("No loans found for this user.");
        } else {
            for (Loan loan : loans) {
                System.out.println(loan.toString());
            }
        }
        System.out.println();
    }
    
    // ==================== SEARCH ====================
    
    private void searchDocumentsMenu() {
        System.out.println("\n=== SEARCH DOCUMENTS ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by ID");
        System.out.println("4. Global Search");
        System.out.println("0. Back to Main Menu");
        System.out.println();
        
        int choice = readIntInput("Enter choice: ");
        
        if (choice == 0) return;
        
        String query = readInput("Enter search query: ");
        
        SearchStrategy strategy;
        switch (choice) {
            case 1:
                strategy = new TitleSearchStrategy();
                break;
            case 2:
                strategy = new AuthorSearchStrategy();
                break;
            case 3:
                strategy = new IdSearchStrategy();
                break;
            case 4:
                strategy = new GlobalSearchStrategy();
                break;
            default:
                System.out.println("Invalid choice.\n");
                return;
        }
        
        List<Document> results = libraryService.searchDocuments(query, strategy);
        
        System.out.println("\n=== SEARCH RESULTS ===");
        if (results.isEmpty()) {
            System.out.println("No documents found.");
        } else {
            System.out.println("Found " + results.size() + " document(s):");
            for (Document doc : results) {
                System.out.println(doc.toString());
            }
        }
        System.out.println();
    }
    
    // ==================== STATISTICS ====================
    
    private void displayStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        Map<String, Integer> stats = libraryService.getStatistics();
        
        System.out.println("Total Documents: " + stats.get("totalDocuments"));
        System.out.println("Available Documents: " + stats.get("availableDocuments"));
        System.out.println("Total Users: " + stats.get("totalUsers"));
        System.out.println("Active Loans: " + stats.get("activeLoans"));
        System.out.println("Overdue Loans: " + stats.get("overdueLoans"));
        System.out.println();
    }
    
    // ==================== INPUT HELPERS ====================
    
    private String readInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return InputValidator.sanitize(input);
    }
    
    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private LocalDate parseDate(String dateStr) throws LibraryException {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new LibraryException("Invalid date format. Use yyyy-MM-dd");
        }
    }
}
