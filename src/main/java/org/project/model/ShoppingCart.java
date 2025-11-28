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

    private HashMap<Long, Integer> bookCount = new HashMap<Long, Integer>();

    public ShoppingCart() {}

//    public ShoppingCart(int userId) {
//        this.userId = 1;
//    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public void addBook (Book book) {
        if (book != null) {
            if(!this.bookList.contains(book)){
                this.bookList.add(book);
            }
        }
        long isbn = book.getISBN();
        this.bookCount.put(isbn, this.bookCount.getOrDefault(isbn, 0) + 1);
        this.decreaseBookInventory(book);
    }

    public void removeBook (Book book) {
        long isbn = book.getISBN();
        if(this.bookCount.containsKey(isbn) && this.bookCount.get(isbn) >= 1){
            this.bookCount.put(isbn, this.bookCount.get(isbn) - 1);
            if(this.bookCount.get(isbn) == 0){
                this.bookList.remove(book);
            }
            this.increaseBookInventory(book);
        }
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Book book : bookList) {
            totalPrice += book.getPrice() * this.getBookCountByISBN(book.getISBN());
        }
        return totalPrice;
    }

    public void increaseBookInventory(Book book){
        Book b = getBook(book.getISBN());
        if(b!=null) b.setInventory(b.getInventory() + 1);
    }

    public void decreaseBookInventory(Book book){
        Book b = getBook(book.getISBN());
        if(b!=null) b.setInventory(b.getInventory() - 1);
    }

    private Book getBook(long ISBN){
        for(Book b: bookList){
            if(b.getISBN() == ISBN){
                return b;
            }
        }
        return null;
    }


    public void clearBooks() {
        bookList.clear();
    }

    public Map<Long, Integer> getBookCounts() {
        return this.bookCount;
    }

    public int getBookCountByISBN(long ISBN) {
        return this.bookCount.get(ISBN);
    }

}
