package org.project.model;

import jakarta.persistence.*;

import java.util.ArrayList;
@Entity
public class Rating {
    public enum Level{
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE;

        public int toInt() {
            switch (this) {
                case ONE: return 1;
                case TWO: return 2;
                case THREE: return 3;
                case FOUR: return 4;
                case FIVE: return 5;
            }
            return 0; // fallback
        }

        public static Level fromInt(int val) {
            switch (val) {
                case 1: return ONE;
                case 2: return TWO;
                case 3: return THREE;
                case 4: return FOUR;
                case 5: return FIVE;
                default: throw new IllegalArgumentException("Invalid rating level");
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Level ratingLevel;

    private String review;

    //private ArrayList<String> reviews = new ArrayList<>();
    //private long ratingNumber = 0;

    public  Rating() {}

    public Rating(Book book, User user, Level ratingLevel,  String review) {
        this.book = book;
        this.user = user;
        this.ratingLevel = ratingLevel;
        this.review = review;
    }

    public int getRatingValue() {
        return ratingLevel.toInt();
    }

    public Level getRatingLevel(){
        return ratingLevel;
    }
    public void setRatingLevel(Level ratingLevel){
        this.ratingLevel = ratingLevel;
    }
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
//    public ArrayList<String> reviews(){
//        return reviews;
//    }
//    public void addReview(String review){
//        reviews.add(review);
//        ratingNumber++;
//    }
//    public long getRatingNumber(){
//        return ratingNumber;
//    }
}

