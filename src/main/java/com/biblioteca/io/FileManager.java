package com.biblioteca.io;

import com.biblioteca.exception.LibraryException;
import com.biblioteca.util.LibraryLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class for file I/O operations.
 * Handles reading and writing data to files with proper error handling.
 */
public class FileManager {
    
    private static final LibraryLogger logger = LibraryLogger.getInstance();
    private final String dataDirectory;
    
    public FileManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        ensureDirectoryExists();
    }
    
    /**
     * Ensure data directory exists
     */
    private void ensureDirectoryExists() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                logger.info("Created data directory: " + dataDirectory);
            } else {
                logger.warning("Failed to create data directory: " + dataDirectory);
            }
        }
    }
    
    /**
     * Write text data to file
     * 
     * @param filename File name
     * @param data Data to write
     * @throws LibraryException if write fails
     */
    public void writeTextFile(String filename, List<String> data) throws LibraryException {
        String filepath = dataDirectory + File.separator + filename;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
            logger.info("Successfully wrote to file: " + filename);
        } catch (IOException e) {
            logger.error("Failed to write to file: " + filename, e);
            throw new LibraryException("Failed to save data to file", e);
        }
    }
    
    /**
     * Read text data from file
     * 
     * @param filename File name
     * @return List of lines read from file
     * @throws LibraryException if read fails
     */
    public List<String> readTextFile(String filename) throws LibraryException {
        String filepath = dataDirectory + File.separator + filename;
        List<String> lines = new ArrayList<>();
        
        File file = new File(filepath);
        if (!file.exists()) {
            logger.warning("File does not exist: " + filename);
            return lines; // Return empty list if file doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            logger.info("Successfully read from file: " + filename + " (" + lines.size() + " lines)");
        } catch (IOException e) {
            logger.error("Failed to read from file: " + filename, e);
            throw new LibraryException("Failed to load data from file", e);
        }
        
        return lines;
    }
    
    /**
     * Write object to file using serialization
     * 
     * @param filename File name
     * @param object Object to serialize
     * @throws LibraryException if serialization fails
     */
    public void writeObject(String filename, Serializable object) throws LibraryException {
        String filepath = dataDirectory + File.separator + filename;
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filepath))) {
            oos.writeObject(object);
            logger.info("Successfully serialized object to file: " + filename);
        } catch (IOException e) {
            logger.error("Failed to serialize object to file: " + filename, e);
            throw new LibraryException("Failed to save object to file", e);
        }
    }
    
    /**
     * Read object from file using deserialization
     * 
     * @param filename File name
     * @return Deserialized object
     * @throws LibraryException if deserialization fails
     */
    public Object readObject(String filename) throws LibraryException {
        String filepath = dataDirectory + File.separator + filename;
        
        File file = new File(filepath);
        if (!file.exists()) {
            logger.warning("File does not exist: " + filename);
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filepath))) {
            Object obj = ois.readObject();
            logger.info("Successfully deserialized object from file: " + filename);
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Failed to deserialize object from file: " + filename, e);
            throw new LibraryException("Failed to load object from file", e);
        }
    }
    
    /**
     * Check if file exists
     */
    public boolean fileExists(String filename) {
        String filepath = dataDirectory + File.separator + filename;
        return new File(filepath).exists();
    }
    
    /**
     * Delete file
     */
    public boolean deleteFile(String filename) {
        String filepath = dataDirectory + File.separator + filename;
        File file = new File(filepath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                logger.info("Deleted file: " + filename);
            }
            return deleted;
        }
        return false;
    }
}
