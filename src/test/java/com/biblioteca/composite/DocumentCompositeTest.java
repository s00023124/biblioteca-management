package com.biblioteca.composite;

import com.biblioteca.model.Book;
import com.biblioteca.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Composite Pattern implementation
 */
public class DocumentCompositeTest {
    
    private Document book1, book2, book3;
    private DocumentCategory rootCategory;
    private DocumentCategory fictionCategory;
    private DocumentCategory scienceCategory;
    
    @BeforeEach
    public void setUp() {
        book1 = new Book("B001", "Fiction Book 1", "Author A", LocalDate.now(),
                        "ISBN1", 100, "Fiction");
        book2 = new Book("B002", "Fiction Book 2", "Author B", LocalDate.now(),
                        "ISBN2", 200, "Fiction");
        book3 = new Book("B003", "Science Book", "Author C", LocalDate.now(),
                        "ISBN3", 300, "Science");
        
        rootCategory = new DocumentCategory("Library");
        fictionCategory = new DocumentCategory("Fiction");
        scienceCategory = new DocumentCategory("Science");
    }
    
    @Test
    public void testLeafNode() {
        DocumentLeaf leaf = new DocumentLeaf(book1);
        
        assertEquals("Fiction Book 1", leaf.getName());
        assertFalse(leaf.isComposite());
        assertEquals(1, leaf.getDocuments().size());
        assertEquals(book1, leaf.getDocuments().get(0));
    }
    
    @Test
    public void testLeafCannotAddChildren() {
        DocumentLeaf leaf = new DocumentLeaf(book1);
        DocumentLeaf anotherLeaf = new DocumentLeaf(book2);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            leaf.add(anotherLeaf);
        });
    }
    
    @Test
    public void testLeafCannotRemoveChildren() {
        DocumentLeaf leaf = new DocumentLeaf(book1);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            leaf.remove(leaf);
        });
    }
    
    @Test
    public void testEmptyCategory() {
        assertTrue(fictionCategory.isComposite());
        assertEquals(0, fictionCategory.getChildren().size());
        assertEquals(0, fictionCategory.getDocuments().size());
        assertEquals("Fiction", fictionCategory.getName());
    }
    
    @Test
    public void testCategoryWithDocuments() {
        fictionCategory.add(new DocumentLeaf(book1));
        fictionCategory.add(new DocumentLeaf(book2));
        
        assertEquals(2, fictionCategory.getChildren().size());
        assertEquals(2, fictionCategory.getDocuments().size());
        assertEquals(2, fictionCategory.getTotalDocumentCount());
    }
    
    @Test
    public void testNestedCategories() {
        // Add books to subcategories
        fictionCategory.add(new DocumentLeaf(book1));
        fictionCategory.add(new DocumentLeaf(book2));
        scienceCategory.add(new DocumentLeaf(book3));
        
        // Add subcategories to root
        rootCategory.add(fictionCategory);
        rootCategory.add(scienceCategory);
        
        // Test root category
        assertEquals(2, rootCategory.getChildren().size());
        assertEquals(3, rootCategory.getTotalDocumentCount());
        
        List<Document> allDocs = rootCategory.getDocuments();
        assertEquals(3, allDocs.size());
        assertTrue(allDocs.contains(book1));
        assertTrue(allDocs.contains(book2));
        assertTrue(allDocs.contains(book3));
    }
    
    @Test
    public void testRemoveFromCategory() {
        DocumentLeaf leaf1 = new DocumentLeaf(book1);
        DocumentLeaf leaf2 = new DocumentLeaf(book2);
        
        fictionCategory.add(leaf1);
        fictionCategory.add(leaf2);
        assertEquals(2, fictionCategory.getChildren().size());
        
        fictionCategory.remove(leaf1);
        assertEquals(1, fictionCategory.getChildren().size());
        assertEquals(1, fictionCategory.getTotalDocumentCount());
    }
    
    @Test
    public void testFindDocumentById() {
        fictionCategory.add(new DocumentLeaf(book1));
        fictionCategory.add(new DocumentLeaf(book2));
        rootCategory.add(fictionCategory);
        
        Document found = rootCategory.findDocumentById("B001");
        assertNotNull(found);
        assertEquals(book1, found);
        
        Document notFound = rootCategory.findDocumentById("NONEXISTENT");
        assertNull(notFound);
    }
    
    @Test
    public void testDeepNesting() {
        DocumentCategory level1 = new DocumentCategory("Level1");
        DocumentCategory level2 = new DocumentCategory("Level2");
        DocumentCategory level3 = new DocumentCategory("Level3");
        
        level3.add(new DocumentLeaf(book1));
        level2.add(level3);
        level1.add(level2);
        rootCategory.add(level1);
        
        assertEquals(1, rootCategory.getTotalDocumentCount());
        assertEquals(book1, rootCategory.findDocumentById("B001"));
    }
}
