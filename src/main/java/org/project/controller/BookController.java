package org.project.controller;


import org.project.model.Book;
import org.project.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/get-book-list")
    public String getBookList(Model model){
        model.addAttribute("bookList", bookRepo.findAll());
        model.addAttribute("book", new Book());
        return "book-list";
    }

    @GetMapping("/sortFragment/{attribute}")
    public String sortByAttribute(@PathVariable String attribute, Model model){
        model.addAttribute("bookList", bookRepo.findAll(Sort.by(attribute).ascending()));
        System.out.println("pulling list sorted by " + attribute);
        return "/fragments/bookTable";
    }

    @PostMapping("/add-book")
    public String createBook(@ModelAttribute Book book){
        bookRepo.save(book);
        return "redirect:/get-book-list";
    }

    @PostMapping("/delete-book/{id}")
    public String deleteBook(@PathVariable int id){
        bookRepo.deleteById(id);
        return "redirect:/get-book-list";
    }

    @GetMapping("/edit-book/{id}")
    public String editBook(@PathVariable int id, Model model){
        Book book = bookRepo.findBookById(id);
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update-book")
    public String updateBook(@ModelAttribute Book book){
        bookRepo.save(book);
        return "redirect:/get-book-list";
    }

}
