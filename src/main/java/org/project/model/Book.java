package org.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.util.List;

@Entity
public class Book {
    //ISBN, picture, description, author, publisher,.

    @Id
    private int ISBN;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private int inventory;
    private double price;
    private int pageCount;

    @Lob
    private byte[] pictureFile;

    private List<String> genres;

    public  Book() {
    }

    public Book(int ISBN, String title, String author, String publisher, String description,int  inventory,double price) {

        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.inventory = inventory;
        this.price = price;
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

    public int getInventory() {return inventory;}
    public void setInventory(int inventory) {this.inventory = inventory;}

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}
    //Add getter and setter for picture file?
    public  byte[] getPictureFile() {return pictureFile;}
    public void setPictureFile(byte[] pictureFile) {this.pictureFile = pictureFile;}

    public List<String> getGenres() {return genres;}
    public void setGenres(List<String> genres) {this.genres = genres;}

    public int getPageCount() {return pageCount;}
    public void setPageCount(int pageCount) {this.pageCount = pageCount;}
}
