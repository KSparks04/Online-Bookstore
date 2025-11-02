package org.project.controller;


import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/sortFragment/{attribute}/{ascending}")
    public String sortByAttribute(@PathVariable String attribute, @PathVariable Boolean ascending, Model model){
        model.addAttribute("bookList", ascending ? bookRepo.findAll(Sort.by(attribute).ascending()) : bookRepo.findAll(Sort.by(attribute).descending()));
        return "fragments/book-table";
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

}
