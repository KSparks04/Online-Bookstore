package org.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class BookStore {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "bookStore", cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private List<BookInventory> inventories = new ArrayList<>();
    
    public BookStore(){}

    public void addBook(Book book, Integer inventory){
        if(book == null || book.getISBN() == null) return;

        //ignore if book already exists
        for(BookInventory bi : inventories){
            if(bi.getBook().getISBN().equals(book.getISBN())){
                bi.setInventory(inventory);
                return;
            }
        }

        //create new inventory
        BookInventory bi = new BookInventory(this, book, inventory);
        inventories.add(bi);
    }

    public void removeBook(String ISBN){
        inventories.removeIf(bi -> bi.getBook().getISBN().equals(ISBN));
    }

    public Integer getInventory(String ISBN){
        return inventories.stream()
            .filter(bi -> bi.getBook().getISBN().equals(ISBN))
            .map(BookInventory :: getInventory)
            .findFirst()
            .orElse(null);
    }

    public void changeInventory(String ISBN, Integer inventory){
        for(BookInventory bi: inventories){
            if(bi.getBook().getISBN().equals(ISBN)){
                bi.setInventory(inventory);
                return;
            }
        }
    }

    public Long getId(){return id;}
}

