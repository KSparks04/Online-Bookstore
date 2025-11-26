package org.project.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links each purchase to a user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Links to the purchased book
    @ManyToOne
    @JoinColumn(name = "book_ISBN", nullable = false)
    private Book book;

    private int quantity;

    private LocalDateTime purchaseDate;

    public Purchase() {}

    public Purchase(User user, Book book, int quantity) {
        this.user = user;
        this.book = book;
        this.quantity = quantity;
        this.purchaseDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Book getBook() { return book; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public int getQuantity(){return quantity;}

    public void setUser(User user) { this.user = user; }
    public void setBook(Book book) { this.book = book; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
    public void setQuantity(int quantity){this.quantity = quantity;}
}
