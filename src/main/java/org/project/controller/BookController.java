package org.project.controller;


import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepo;
    @ModelAttribute("genres")
    public List<String> genres() {
        List<String> genres = new ArrayList<>();
        genres.add("Fiction");
        genres.add("Non-Fiction");
        genres.add("Romance");
        genres.add("YA");
        genres.add("Science Fiction");
        genres.add("Comedy");
        genres.add("Action");
        genres.add("Horror");
        genres.add("Fantasy");
        genres.add("Mystery");
        genres.add("Dystopian");
        genres.add("History");
        return genres;
    }
    @GetMapping("/get-book-list")
    public String getBookList(
            @RequestParam(required = false, defaultValue = "") String function,
            @RequestParam(required = false) String variable,
            Model model, HttpSession session) {

        Iterable<Book> bookList = null;

        switch (function) {
            case "search":
                if (variable != null && !variable.trim().isEmpty()) {
                    bookList = bookRepo.findByAllColumns(variable.toLowerCase());

                    // If search returns nothing, show the not-found page
                    if (!bookList.iterator().hasNext()) {
                        model.addAttribute("searchQuery", variable);
                        return "error/book-not-found";
                    }
                } else {
                    // If search query is empty, just show all books
                    bookList = bookRepo.findAll();
                }
                break;

            case "refresh":
                bookList = bookRepo.findAll();
                model.addAttribute("bookList", bookList);
                model.addAttribute("book", new Book());
                return "fragments/book-table";

            default:
                bookList = bookRepo.findAll();
                break;
        }

        model.addAttribute("bookList", bookList);
        model.addAttribute("book", new Book());
        model.addAttribute("genres", genres());
        ShoppingCartController.addShoppingCartAttributes(model, session);
        return "book-list";
    }

    @GetMapping("/sortFragment/{attribute}/{ascending}")
    public String sortByAttribute(@PathVariable String attribute, @PathVariable Boolean ascending, Model model){
        model.addAttribute("bookList", ascending ? bookRepo.findAll(Sort.by(attribute).ascending()) : bookRepo.findAll(Sort.by(attribute).descending()));
        return "fragments/book-table";
    }

    @PostMapping("/add-book")
    public String createBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model, @RequestParam ("pictureUpload") MultipartFile file){
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();
                book.setPictureFile(bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(bookRepo.existsById(book.getISBN())){
            bindingResult.rejectValue("ISBN", "error.book", "ISBN already exists");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("bookList", bookRepo.findAll());
            return "fragments/book-form";
        }

        bookRepo.save(book);
        model.addAttribute("bookList", bookRepo.findAll());
        model.addAttribute("book", new Book());
        return "fragments/book-form";
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
    public String updateBook(@ModelAttribute Book book, @RequestParam ("pictureUpload") MultipartFile file){
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();
                book.setPictureFile(bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        bookRepo.save(book);
        return "redirect:/get-book-list";
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable("id") int id, Model model, HttpSession session) {
        Book book = bookRepo.findByISBN(id);
        if (book == null) {
            // Book not found, show a dedicated error page
            return "error/book-not-found";
        }
        model.addAttribute("book", book);
        ShoppingCartController.addShoppingCartAttributes(model, session);
        return "book";
    }
    @GetMapping("/book-image/{ISBN}")
    public ResponseEntity<byte[]> getBookImage(@PathVariable int ISBN){
        Book book =bookRepo.findByISBN(ISBN);
        byte[] imageBytes = book.getPictureFile();
        if(imageBytes == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
