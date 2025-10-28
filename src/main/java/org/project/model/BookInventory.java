package org.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BookInventory {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    private BookStore bookStore;

    @ManyToOne
    private Book book;

    private Integer inventory;

    public BookInventory(){}

    public BookInventory(BookStore bookStore, Book book, Integer inventory){
        this.bookStore = bookStore;
        this.book = book;
        this.inventory = inventory;
    }

    public Long getId(){
        return id;
    }

    public BookStore getBookStore(){return bookStore;}
    public void setBookStore(BookStore bookStore){this.bookStore = bookStore;}
    
    public Book getBook(){return book;}
    public void setBook(Book book){this.book = book;}

    public Integer getInventory(){return inventory;}
    public void setInventory(Integer inventory){this.inventory = inventory;}

}
