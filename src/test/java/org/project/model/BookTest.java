package org.project.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest {
    Book  book;

    @Before
    public void setUp() throws Exception {
        book = new Book(128791,"Coding 101","John Smith","Ravens","Learn",15,29.99, 111);
    }

    @Test
    public void getISBN() {
        assertEquals(128791,book.getISBN());

    }

    @Test
    public void setISBN() {
        book.setISBN(128712);
        assertEquals(128712,book.getISBN());
    }
    @Test
    public void getTitle() {
        assertEquals("Coding 101",book.getTitle());
    }
    @Test
    public void setTitle() {
        book.setTitle("Coding Expertise");
        assertEquals("Coding Expertise",book.getTitle());
    }
    @Test
    public void getAuthor() {
        assertEquals("John Smith",book.getAuthor());
    }
    @Test
    public void setAuthor() {
        book.setAuthor("John.H Smith");
        assertEquals("John.H Smith",book.getAuthor());
    }
    @Test
    public void getPublisher() {
        assertEquals("Ravens",book.getPublisher());
    }
    @Test
    public void setPublisher() {
        book.setPublisher("Birds");
        assertEquals("Birds",book.getPublisher());
    }
    @Test
    public void getDescription() {
        assertEquals("Learn",book.getDescription());
    }
    @Test
    public void setDescription() {
        book.setDescription("Learning is fun.");
        assertEquals("Learning is fun.",book.getDescription());
    }
    @Test
    public void getPrice() {
        assertEquals(29.99,book.getPrice(),0);
    }
    @Test
    public void setPrice() {
        book.setPrice(12.99);
        assertEquals(12.99,book.getPrice(),0);
    }
    @Test
    public void getInventory() {
        assertEquals(15,book.getInventory());
    }
    @Test
    public void setInventory() {
        book.setInventory(25);
        assertEquals(25,book.getInventory());
    }
    @Test
    public void getPageCount(){
        assertEquals(111, book.getPageCount());
    }
    @Test
    public void setPageCount(){
        book.setPageCount(123);
        assertEquals(123, book.getPageCount());
    }
}