package org.project.model;

import java.util.Optional;

import org.project.repositories.BookRepo;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class BookStore {
    private final BookRepo bookRepo;
    
    public BookStore(BookRepo bookRepo){this.bookRepo = bookRepo;}

    public Book getBook(String ISBN){
        Optional<Book> existing = bookRepo.findByISBN(ISBN);
        if(existing.isPresent()){
            return existing.get();
        }else{
            return null;
        }
    }

    public void addBook(Book book){
        if(book == null || book.getISBN() == null) return;

        //skip if book is already in database
        if(getBook(book.getISBN())!=null) return;

        bookRepo.save(book);
    }

    public void removeBook(String ISBN){
        bookRepo.deleteByISBN(ISBN);
    }

    
}

