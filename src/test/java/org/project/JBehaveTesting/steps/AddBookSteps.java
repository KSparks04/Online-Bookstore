package org.project.JBehaveTesting.steps;

import org.jbehave.core.annotations.*;
import org.project.model.Book;
import org.project.model.Series;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AddBookSteps {

    // Mock repository to simulate the real one
    private Map<Integer, Book> mockBookRepo = new HashMap<>();
    private Book addedBook;

    @Given("the bookstore is empty")
    public void bookstoreIsEmpty() {
        mockBookRepo.clear();
        System.out.println("Bookstore cleared");
    }

    @When("I add a book with ISBN $isbn, title $title, author $author, publisher $publisher, description $description, inventory $inventory, price $price, page count $pageCount, series $seriesName")
    public void addBook(int isbn, String title, String author, String publisher, String description,
                        int inventory, double price, int pageCount, String seriesName) {
        // Create book and series objects
        Book book = new Book(isbn, title, author, publisher, description, inventory, price, pageCount);
        Series series = new Series(seriesName);
        book.setSeries(series);

        // Add to mock repository
        mockBookRepo.put(isbn, book);
        addedBook = mockBookRepo.get(isbn);

        System.out.println("Book added: " + addedBook.getTitle());
    }

    @Then("the book with ISBN $isbn should exist in the system")
    public void bookShouldExist(int isbn) {
        assertTrue(mockBookRepo.containsKey(isbn), "Book should exist in the system");
        assertEquals(isbn, mockBookRepo.get(isbn).getISBN(), "Book ISBN should match");
        System.out.println("Verified book exists: " + mockBookRepo.get(isbn).getTitle());
    }
}