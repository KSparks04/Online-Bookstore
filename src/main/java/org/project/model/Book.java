    package org.project.model;

import jakarta.persistence.Entity;
    import jakarta.persistence.Id;
    import jakarta.persistence.Lob;

    import jakarta.validation.constraints.*;;

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
        @Min(value = 1, message = "Inventory must be equal to or greater than 1")
        private int inventory;
        @NotNull(message = "Price is required")
        @Min(value = 0,  message = "Price must be non-negative")
        private double price;
        //Eventually picture
        @Lob
        private byte[] pictureFile;
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
    }
