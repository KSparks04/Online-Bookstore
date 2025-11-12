    package org.project.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;

import java.util.*;

    @Entity
    public class Book {
        //ISBN, picture, description, author, publisher,.

        @Id
        @Min(value = 1, message = "ISBN must be a non-negative number")
        private int ISBN;
        @NotBlank(message = "Title is required")
        private String title;
        @NotBlank(message = "Author is required")
        private String author;
        @NotBlank(message = "Publisher is required")
        private String publisher;
        @NotBlank(message = "Description is required")
        private String description;
        @Min(value = 0, message = "Inventory must be equal to or greater than 0")
        private int inventory;
        @NotNull(message = "Price is required")
        @Min(value = 0,  message = "Price must be non-negative")
        private double price;
        @NotNull(message="Page count is required")
        @Min(value = 1, message = "Page count must be equal to or greater than 1")
        private int pageCount;
        //Eventually picture
        @Lob
        private byte[] pictureFile;

        private List<String> genres;
        @ManyToOne
        private Series series;
        @OneToMany( cascade = CascadeType.ALL)
        private List<Rating> ratings = populateRatings();


        public  Book() {

        }

        public Book(int ISBN, String title, String author, String publisher, String description, int inventory, double price, int pageCount) {

            this.ISBN = ISBN;
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.description = description;
            this.inventory = inventory;
            this.price = price;
            this.pageCount = pageCount;

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
        public  Series getSeries() {return series;}
        public void setSeries(Series series) {this.series = series;}
        public List<Rating> getRatings() {return ratings;}
        public void setRatings(List<Rating> ratings) {this.ratings = ratings;}
        private List<Rating> populateRatings(){
            this.ratings = new ArrayList<>();
            ratings.add(new Rating(Rating.Level.ONE));
            ratings.add(new Rating(Rating.Level.TWO));
            ratings.add(new Rating(Rating.Level.THREE));
            ratings.add(new Rating(Rating.Level.FOUR));
            ratings.add(new Rating(Rating.Level.FIVE));
            return ratings;

        }
        @Override
        public boolean equals(Object o){
            if(this == o) return true;

            Book book = (Book) o;
            return this.ISBN == book.ISBN && Objects.equals(this.title, book.title) && Objects.equals(this.author, book.author)
                    && Objects.equals(this.publisher, book.publisher) && this.price == book.price;
        }
    }
