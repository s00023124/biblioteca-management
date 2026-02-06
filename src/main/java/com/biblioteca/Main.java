package com.biblioteca;

import com.biblioteca.io.DataPersistence;
import com.biblioteca.io.FileManager;
import com.biblioteca.service.LibraryService;
import com.biblioteca.ui.ConsoleUI;
import com.biblioteca.util.LibraryLogger;

/**
 * Main entry point for the Library Management System.
 */
public class Main {
    
    public static void main(String[] args) {
        LibraryLogger logger = LibraryLogger.getInstance();
        logger.setLogLevel(LibraryLogger.LogLevel.INFO);
        logger.info("=== Library Management System Starting ===");
        
        try {
            String dataDirectory = "data";
            FileManager fileManager = new FileManager(dataDirectory);
            DataPersistence dataPersistence = new DataPersistence(fileManager);
            LibraryService libraryService = new LibraryService(dataPersistence);
            
            ConsoleUI consoleUI = new ConsoleUI(libraryService);
            consoleUI.start();
            
            logger.info("=== Library Management System Shutting Down ===");
            
        } catch (Exception e) {
            logger.error("Fatal error in main application", e);
            System.err.println("Application failed to start: " + e.getMessage());
            System.err.println("Please check the log file for details.");
            System.exit(1);
        }
    }
}
