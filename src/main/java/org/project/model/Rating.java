package org.project.model;

import java.util.ArrayList;

public class Rating {
    enum Level{
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
    }
    private Level ratingLevel;
    private ArrayList<String> reviews;

    public Rating(Level ratingLevel){
        this.ratingLevel = ratingLevel;
    }
    public Level ratingLevel(){
        return ratingLevel;
    }
    public ArrayList<String> reviews(){
        return reviews;
    }
    public void addReview(String review){
        reviews.add(review);
    }
}

