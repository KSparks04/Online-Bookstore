package org.project.model;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, BookInventory> inventoryMap = new HashMap<>();
    
    public BookStore(){}

    public void addBook(Book book, Integer inventory){
        if(book == null || book.getISBN() == null) return;
        BookInventory bi = new BookInventory(this, book, inventory);
        inventoryMap.put(book.getISBN(), bi);
    }

    public void removeBook(String ISBN){
        inventoryMap.remove(ISBN);
    }

    public Integer getInventory(String ISBN){
        BookInventory bi = inventoryMap.get(ISBN);
        return(bi != null) ? bi.getInventory() : null;
    }

    public void changeInventory(String ISBN, Integer inventory){
        BookInventory bi = inventoryMap.get(ISBN);
        if(bi!= null){
            bi.setInventory(inventory);
        }
    }

    public Long getId(){return id;}

    public Map<String, BookInventory> getInventoryMap(){return inventoryMap;}
    public void setInventoryMap(Map<String, BookInventory> inventoryMap){this.inventoryMap = inventoryMap;}
}

