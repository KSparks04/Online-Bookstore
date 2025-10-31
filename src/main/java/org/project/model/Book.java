package org.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Book {
    //ISBN, picture, description, author, publisher,.

    @Id
    private int ISBN;
    private String title;
    private String author;
    private String publisher;
    private String description;
    //Eventually picture
    public  Book() {
    }

    public Book(int ISBN, String title, String author, String publisher, String description) {

        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
    }

    public int getISBN() {return  ISBN;}
    public void setISBN(int ISBN) {this.ISBN = ISBN;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getAuthor(){return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
