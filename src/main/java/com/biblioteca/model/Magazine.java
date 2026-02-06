package com.biblioteca.model;

import java.time.LocalDate;

/**
 * Represents a magazine in the library system.
 */
public class Magazine extends Document {
    private static final long serialVersionUID = 1L;
    
    private int issueNumber;
    private String publisher;
    private String frequency;
    
    public Magazine(String id, String title, String author, LocalDate publicationDate,
                    int issueNumber, String publisher, String frequency) {
        super(id, title, author, publicationDate, DocumentType.MAGAZINE);
        this.issueNumber = issueNumber;
        this.publisher = publisher;
        this.frequency = frequency;
    }
    
    @Override
    protected String getSpecificInfo() {
        return String.format("Issue: #%d\nPublisher: %s\nFrequency: %s\n", 
                           issueNumber, publisher, frequency);
    }
    
    @Override
    protected String getDocumentTypeName() {
        return "MAGAZINE";
    }
    
    public int getIssueNumber() {
        return issueNumber;
    }
    
    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
