package com.biblioteca.strategy;

import com.biblioteca.model.Book;
import com.biblioteca.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Strategy Pattern implementation
 */
public class SearchStrategyTest {
    
    private List<Document> documents;
    private SearchContext searchContext;
    
    @BeforeEach
    public void setUp() {
        documents = new ArrayList<>();
        
        documents.add(new Book("B001", "Java Programming", "John Doe", 
                              LocalDate.now(), "ISBN1", 400, "Tech"));
        documents.add(new Book("B002", "Python Basics", "Jane Smith",
                              LocalDate.now(), "ISBN2", 300, "Tech"));
        documents.add(new Book("B003", "Data Structures", "John Doe",
                              LocalDate.now(), "ISBN3", 500, "CS"));
        documents.add(new Book("B004", "Web Development", "Bob Johnson",
                              LocalDate.now(), "ISBN4", 350, "Tech"));
        
        searchContext = new SearchContext();
    }
    
    @Test
    public void testTitleSearchStrategy() {
        searchContext.setStrategy(new TitleSearchStrategy());
        
        List<Document> results = searchContext.executeSearch(documents, "java");
        assertEquals(1, results.size());
        assertEquals("Java Programming", results.get(0).getTitle());
        
        results = searchContext.executeSearch(documents, "Programming");
        assertEquals(1, results.size());
        
        results = searchContext.executeSearch(documents, "xyz");
        assertEquals(0, results.size());
    }
    
    @Test
    public void testAuthorSearchStrategy() {
        searchContext.setStrategy(new AuthorSearchStrategy());
        
        List<Document> results = searchContext.executeSearch(documents, "john doe");
        assertEquals(2, results.size());
        
        results = searchContext.executeSearch(documents, "jane");
        assertEquals(1, results.size());
        assertEquals("Python Basics", results.get(0).getTitle());
        
        results = searchContext.executeSearch(documents, "nonexistent");
        assertEquals(0, results.size());
    }
    
    @Test
    public void testIdSearchStrategy() {
        searchContext.setStrategy(new IdSearchStrategy());
        
        List<Document> results = searchContext.executeSearch(documents, "B001");
        assertEquals(1, results.size());
        assertEquals("B001", results.get(0).getId());
        
        results = searchContext.executeSearch(documents, "B999");
        assertEquals(0, results.size());
    }
    
    @Test
    public void testGlobalSearchStrategy() {
        searchContext.setStrategy(new GlobalSearchStrategy());
        
        // Search by title
        List<Document> results = searchContext.executeSearch(documents, "java");
        assertEquals(1, results.size());
        
        // Search by author (finds "John Doe" x2 and "Bob Johnson")
        results = searchContext.executeSearch(documents, "john");
        assertEquals(3, results.size());
        
        // Search by ID
        results = searchContext.executeSearch(documents, "B002");
        assertEquals(1, results.size());
        
        // Search term matching multiple documents
        results = searchContext.executeSearch(documents, "Data");
        assertTrue(results.size() >= 1);
    }
    
    @Test
    public void testEmptyQuery() {
        searchContext.setStrategy(new TitleSearchStrategy());
        
        List<Document> results = searchContext.executeSearch(documents, "");
        assertEquals(0, results.size());
        
        results = searchContext.executeSearch(documents, null);
        assertEquals(0, results.size());
    }
    
    @Test
    public void testCaseInsensitiveSearch() {
        searchContext.setStrategy(new TitleSearchStrategy());
        
        List<Document> results1 = searchContext.executeSearch(documents, "JAVA");
        List<Document> results2 = searchContext.executeSearch(documents, "java");
        List<Document> results3 = searchContext.executeSearch(documents, "JaVa");
        
        assertEquals(results1.size(), results2.size());
        assertEquals(results2.size(), results3.size());
    }
    
    @Test
    public void testSwitchingStrategies() {
        // Start with title search
        searchContext.setStrategy(new TitleSearchStrategy());
        assertEquals("Title Search", searchContext.getCurrentStrategyName());
        
        List<Document> results = searchContext.executeSearch(documents, "java");
        assertEquals(1, results.size());
        
        // Switch to author search
        searchContext.setStrategy(new AuthorSearchStrategy());
        assertEquals("Author Search", searchContext.getCurrentStrategyName());
        
        results = searchContext.executeSearch(documents, "john");
        assertEquals(3, results.size());
    }
    
    @Test
    public void testDefaultStrategy() {
        // SearchContext uses TitleSearchStrategy by default
        List<Document> results = searchContext.executeSearch(documents, "python");
        assertEquals(1, results.size());
        assertEquals("Python Basics", results.get(0).getTitle());
    }
}
