package com.biblioteca.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom logger with file and console output.
 * Singleton pattern ensures single instance.
 */
public class LibraryLogger {
    
    private static LibraryLogger instance;
    private static final String LOG_FILE = "biblioteca.log";
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private LogLevel currentLogLevel;
    private boolean logToConsole;
    private boolean logToFile;
    
    public enum LogLevel {
        DEBUG(0),
        INFO(1),
        WARNING(2),
        ERROR(3);
        
        private final int priority;
        
        LogLevel(int priority) {
            this.priority = priority;
        }
        
        public int getPriority() {
            return priority;
        }
    }
    
    private LibraryLogger() {
        this.currentLogLevel = LogLevel.INFO;
        this.logToConsole = true;
        this.logToFile = true;
    }
    
    public static synchronized LibraryLogger getInstance() {
        if (instance == null) {
            instance = new LibraryLogger();
        }
        return instance;
    }
    
    public void setLogLevel(LogLevel level) {
        this.currentLogLevel = level;
        info("Log level set to: " + level);
    }
    
    public void setConsoleLogging(boolean enable) {
        this.logToConsole = enable;
    }
    
    public void setFileLogging(boolean enable) {
        this.logToFile = enable;
    }
    
    public void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }
    
    public void info(String message) {
        log(LogLevel.INFO, message, null);
    }
    
    public void warning(String message) {
        log(LogLevel.WARNING, message, null);
    }
    
    public void error(String message) {
        log(LogLevel.ERROR, message, null);
    }
    
    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }
    
    /**
     * Core logging method
     */
    private void log(LogLevel level, String message, Throwable throwable) {
        // Check if this message should be logged based on current log level
        if (level.getPriority() < currentLogLevel.getPriority()) {
            return;
        }
        
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Log to console if enabled
        if (logToConsole) {
            System.out.println(logMessage);
            if (throwable != null) {
                // Don't print full stack trace to console (security)
                System.out.println("  Exception: " + throwable.getClass().getName() + 
                                 ": " + throwable.getMessage());
            }
        }
        
        // Log to file if enabled
        if (logToFile) {
            writeToFile(logMessage, throwable);
        }
    }
    
    /**
     * Write log message to file
     */
    private void writeToFile(String message, Throwable throwable) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println(message);
            
            // Write full stack trace to file only (not to console)
            if (throwable != null) {
                throwable.printStackTrace(pw);
            }
            
        } catch (IOException e) {
            // Fallback to console if file writing fails
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Log method entry (for debugging)
     */
    public void entering(String className, String methodName) {
        debug("ENTERING: " + className + "." + methodName + "()");
    }
    
    /**
     * Log method exit (for debugging)
     */
    public void exiting(String className, String methodName) {
        debug("EXITING: " + className + "." + methodName + "()");
    }
}
