package org.project.JBehaveTesting.steps;

import org.jbehave.core.annotations.*;
import org.project.model.Book;
import org.project.model.Series;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteBookSteps {

    // Mock repository to simulate the real one
    private Map<Integer, Book> mockBookRepo = new HashMap<>();
    private Book deletedBook;

    public DeleteBookSteps() {
        // Populate the mock repository
        Book book = new Book(77777, "Hyrule Warriors", "Kyle Conrad", "Link Inc.", "A thrilling adventure of a young Kokiri", 10, 19.99, 300);
        Series series = new Series("Hyrule Compendium");
        book.setSeries(series);

        // Add to fake repository
        mockBookRepo.put(77777, book);
    }

    @Given("the bookstore is not empty")
    public void bookstoreIsNotEmpty() {
        if (!mockBookRepo.isEmpty()) {
            int repoSize = mockBookRepo.size();
            System.out.println("Bookstore contains books. size = " + repoSize);
        }
    }

    @When("I delete a book with ISBN $isbn")
    public void deleteBook(int isbn) {
        deletedBook = mockBookRepo.get(isbn);
        mockBookRepo.remove(isbn);

        System.out.println("Book deleted: " + deletedBook.getTitle());
    }

    @Then("the book with ISBN $isbn should no longer exist in the system")
    public void bookShouldNotExist(int isbn) {
        assertFalse(mockBookRepo.containsKey(isbn), "Book should not exist in the system");
        assertEquals(isbn, deletedBook.getISBN(), "Book ISBN should match");
        int repoSize = mockBookRepo.size();
        System.out.println("Verified book does not exists: " + "repository size = " + repoSize);
    }
}
