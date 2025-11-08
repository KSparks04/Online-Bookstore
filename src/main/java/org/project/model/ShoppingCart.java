package org.project.model;

import jakarta.persistence.*;

import java.util.ArrayList;

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
        this.bookList.add(book);
    }

    public void removeBook (Book book) {
        this.bookList.remove(book);
    }
}
