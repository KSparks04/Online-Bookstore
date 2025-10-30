package org.project.controller;


import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepo;

    @GetMapping("/get-book-list")
    public String getBookList(Model model){
        model.addAttribute("bookList", bookRepo.findAll());
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
        Book book = bookRepo.findBookByISBN(ISBN);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update-book")
    public String updateBook(@ModelAttribute Book book){
        bookRepo.save(book);
        return "redirect:/get-book-list";
    }

}
