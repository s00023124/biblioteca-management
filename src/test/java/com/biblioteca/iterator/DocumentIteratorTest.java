package com.biblioteca.iterator;

import com.biblioteca.model.Book;
import com.biblioteca.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Iterator Pattern implementation
 */
public class DocumentIteratorTest {
    
    private DocumentCollection collection;
    private Document doc1, doc2, doc3;
    
    @BeforeEach
    public void setUp() {
        collection = new DocumentCollection();
        
        doc1 = new Book("B001", "Book One", "Author A", LocalDate.now(),
                       "ISBN1", 100, "Fiction");
        doc2 = new Book("B002", "Book Two", "Author B", LocalDate.now(),
                       "ISBN2", 200, "Science");
        doc3 = new Book("B003", "Book Three", "Author C", LocalDate.now(),
                       "ISBN3", 300, "History");
    }
    
    @Test
    public void testEmptyCollection() {
        Iterator<Document> iterator = collection.createIterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testSingleElement() {
        collection.add(doc1);
        Iterator<Document> iterator = collection.createIterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(doc1, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testMultipleElements() {
        collection.add(doc1);
        collection.add(doc2);
        collection.add(doc3);
        
        Iterator<Document> iterator = collection.createIterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(doc1, iterator.next());
        assertEquals(1, iterator.getCurrentPosition());
        
        assertTrue(iterator.hasNext());
        assertEquals(doc2, iterator.next());
        assertEquals(2, iterator.getCurrentPosition());
        
        assertTrue(iterator.hasNext());
        assertEquals(doc3, iterator.next());
        assertEquals(3, iterator.getCurrentPosition());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testReset() {
        collection.add(doc1);
        collection.add(doc2);
        
        Iterator<Document> iterator = collection.createIterator();
        
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
        
        iterator.reset();
        assertEquals(0, iterator.getCurrentPosition());
        assertTrue(iterator.hasNext());
        assertEquals(doc1, iterator.next());
    }
    
    @Test
    public void testNextOnEmptyCollection() {
        Iterator<Document> iterator = collection.createIterator();
        assertThrows(NoSuchElementException.class, iterator::next);
    }
    
    @Test
    public void testNextAfterEnd() {
        collection.add(doc1);
        Iterator<Document> iterator = collection.createIterator();
        
        iterator.next();
        assertThrows(NoSuchElementException.class, iterator::next);
    }
    
    @Test
    public void testCollectionSize() {
        assertEquals(0, collection.size());
        
        collection.add(doc1);
        assertEquals(1, collection.size());
        
        collection.add(doc2);
        assertEquals(2, collection.size());
        
        collection.remove(doc1);
        assertEquals(1, collection.size());
    }
    
    @Test
    public void testCollectionIsEmpty() {
        assertTrue(collection.isEmpty());
        collection.add(doc1);
        assertFalse(collection.isEmpty());
    }
}
