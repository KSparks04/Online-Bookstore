package org.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Book {
    //ISBN, picture, description, author, publisher,.
    @GeneratedValue
    @Id
    private Long id;
    private String ISBN;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private int inventory;

    //Default Constructor
    public  Book() {}

    //Getters/Setters
    public Long getId() {return id;    }
    public void setId(Long id) {this.id = id;   }

    public String getISBN() {return  ISBN;}
    public void setISBN(String ISBN) {this.ISBN = ISBN;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getAuthor(){return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public int getInventory(){return inventory;}
    public void setInventory(int inventory){this.inventory = inventory;}
}
