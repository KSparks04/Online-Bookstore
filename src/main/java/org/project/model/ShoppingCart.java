package org.project.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//@Entity
public class ShoppingCart {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
//
//    private int userId;

    private ArrayList<Book> bookList = new ArrayList<>();

    public ShoppingCart() {}

//    public ShoppingCart(int userId) {
//        this.userId = 1;
//    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public void addBook (Book book) {
        if (book != null) {
            this.bookList.add(book);
        }
    }

    public void removeBook (Book book) {
        this.bookList.remove(book);
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Book book : bookList) {
            totalPrice += book.getPrice();
        }
        return totalPrice;
    }

    public void clearBooks() {
        bookList.clear();
    }

    public Map<Long, Integer> getBookCounts() {
        HashMap<Long, Integer> counts = new HashMap<>();
        for (Book book : bookList) {
            long isbn = book.getISBN();
            counts.put(isbn, counts.getOrDefault(isbn, 0) + 1);
        }
        return counts;
    }


}
