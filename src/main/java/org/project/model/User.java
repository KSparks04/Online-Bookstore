package org.project.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "ISBN")
    private List<Book> wishlist;

    @Column(nullable = false)
    private boolean isOwner;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isOwner = false;
        this.wishlist = new ArrayList<>();
    }

    public User(String username, String password, boolean isOwner) {
        this.username = username;
        this.password = password;
        this.isOwner = isOwner;
        this.wishlist = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsOwner(boolean isOwner){
        this.isOwner = isOwner;
    }

    public boolean getIsOwner(){
        return this.isOwner;
    }

    public List<Book> getWishlist() {
        return this.wishlist;
    }

    public void setWishlist(List<Book> wishlist) {
        this.wishlist = wishlist;
    }

    public boolean addBookToWishlist(Book book){
        if(this.wishlist.contains(book)){
            return false;
        }
        this.wishlist.add(book);
        return true;
    }

    public boolean removeBookFromWishlist(Book book){
        return this.wishlist.remove(book);
    }
}
