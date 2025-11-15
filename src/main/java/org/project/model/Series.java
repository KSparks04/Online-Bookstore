package org.project.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seriesCode;
    @Column(unique = true)
    private String seriesName;
    @OneToMany
    private List<Book> bookList;
    public Series() {}
    public Series(String seriesName) {

        this.seriesName = seriesName;
    }
    public int getSeriesCode() {return seriesCode;}
    public void setSeriesCode(int id) {this.seriesCode = id;}
    public String getSeriesName() {return seriesName;}
    public void setSeriesName(String seriesName) {this.seriesName = seriesName;}
    public List<Book> getBookList() {return bookList;}
    public void addBook(Book book) {bookList.add(book);}
}
