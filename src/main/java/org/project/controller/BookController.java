package org.project.controller;


import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    @GetMapping("/get-book-list")
    public String getBookList(@RequestParam(required=false, defaultValue="") String function, @RequestParam(required=false) String variable, Model model){
        //Decide the contents of the book list to display
        Iterable<Book> bookList = null;
        switch (function){
            case "search":
                bookList = bookRepo.findByAllColumns(variable.toLowerCase());
                break;
            default:
                bookList = bookRepo.findAll();
                break;
        }
        model.addAttribute("bookList", bookList);
        model.addAttribute("book", new Book());
        return "book-list";
    }

    @PostMapping("/add-book")
    public String createBook(@ModelAttribute Book book){
        bookRepo.save(book);
        return "redirect:/get-book-list";
    }

    @PostMapping("/delete-book/{ISBN}")
    public String deleteBook(@PathVariable int ISBN){
        bookRepo.deleteById(ISBN);
        return "redirect:/get-book-list";
    }

    @GetMapping("/edit-book/{ISBN}")
    public String editBook(@PathVariable int ISBN, Model model){
        Book book = bookRepo.findByISBN(ISBN);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update-book")
    public String updateBook(@ModelAttribute Book book){
        bookRepo.save(book);
        return "redirect:/get-book-list";
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable("id") int id, Model model) {
        Book book = bookRepo.findByISBN(id);
        model.addAttribute("book", book);
        return "book";
    }

}
