package org.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {
    ShoppingCart cart;
    Book book;

    @BeforeEach
    public void setUp() throws Exception {
        cart = new ShoppingCart();
        book = new Book("128791","Coding 101","John Smith","Ravens","Learn",15,29.99, 111);
    }
    @Test
    void getBookList() {
        assertEquals(0, cart.getBookList().size());
    }

    @Test
    void addBook() {
        cart.addBook(book);
        assertTrue(cart.getBookList().contains(book));
    }

    @Test
    void removeBook() {
        cart.removeBook(book);
        assertEquals(0, cart.getBookList().size());
    }
}