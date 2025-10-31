package org.project.controller;

import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private BookRepository bookRepository;


    //TODO temp probably wanna have a DB with persistence
    private boolean setup = true;
    public void setup(){
        Book book1 = new Book(0000000000001, "Title1", "Author1", "Publisher1", "Description of book 1");
        Book book2 = new Book(0000000000002, "Title2", "Author2", "Publisher2", "Description of book 2");
        Book book3 = new Book(0000000000003, "Title3", "Author3", "Publisher3", "Description of book 3");

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    @GetMapping("/")
    public String home(Model model){
        if (setup){
            setup = false;
            setup();
        }
        return "home";
    }
}
