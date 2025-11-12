package org.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
@Entity
public class Rating {
    enum Level{
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
    }

    private Level ratingLevel;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private ArrayList<String> reviews;
    private long ratingNumber = 0;
    public  Rating() {}
    public Rating(Level ratingLevel){
        this.ratingLevel = ratingLevel;
    }
    public int getRatingLevel(){
        switch (ratingLevel){
            case ONE:
                return 1;
                case TWO:
                    return 2;
                    case THREE:
                        return 3;
                        case FOUR:
                            return 4;
                            case FIVE:
                                return 5;
        }
        return -1;
    }
    public ArrayList<String> reviews(){
        return reviews;
    }
    public void addReview(String review){
        reviews.add(review);
        ratingNumber++;
    }
    public long getRatingNumber(){
        return ratingNumber;
    }
}

