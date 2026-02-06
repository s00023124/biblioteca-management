package com.biblioteca.model;

import java.time.LocalDate;

/**
 * Represents a book in the library system.
 */
public class Book extends Document {
    private static final long serialVersionUID = 1L;
    
    private String isbn;
    private int pages;
    private String genre;
    
    public Book(String id, String title, String author, LocalDate publicationDate, 
                String isbn, int pages, String genre) {
        super(id, title, author, publicationDate, DocumentType.BOOK);
        this.isbn = isbn;
        this.pages = pages;
        this.genre = genre;
    }
    
    @Override
    protected String getSpecificInfo() {
        return String.format("ISBN: %s\nPages: %d\nGenre: %s\n", isbn, pages, genre);
    }
    
    @Override
    protected String getDocumentTypeName() {
        return "BOOK";
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public int getPages() {
        return pages;
    }
    
    public void setPages(int pages) {
        this.pages = pages;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
