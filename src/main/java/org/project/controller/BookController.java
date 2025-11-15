package org.project.controller;


import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.Rating;
import org.project.model.Series;
import org.project.repository.BookRepository;
import org.project.repository.SeriesRepository;
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
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.*;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepo;
    @Autowired
    private SeriesRepository seriesRepo;
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
                model.addAttribute("searchQuery", variable);
                bookList = bookRepo.findByAllColumns(variable);
                break;

            case "refresh":
                bookList = bookRepo.findAll();
                model.addAttribute("bookList", bookList);
                model.addAttribute("book", new Book());
                model.addAttribute("genres", genres());
                model.addAttribute("series",seriesRepo.findAll());
                return "fragments/book-table";

            default:
                bookList = bookRepo.findAll();
                break;
        }

        model.addAttribute("bookList", bookList);
        model.addAttribute("book", new Book());
        model.addAttribute("genres", genres());
        model.addAttribute("series",seriesRepo.findAll());
        ShoppingCartController.addShoppingCartAttributes(model, session);
        return "book-list";
    }
    //Potentially shrink getBookList
    /**@GetMapping("/book-table")
    public String getBookTable(Model model){
        model.addAttribute("bookList", bookRepo.findAll());
        return "fragments/book-table";
    }
    @GetMapping("/book-form")
    public String getBookForm(Model model){
        model.addAttribute("book", new Book());
        model.addAttribute("genres", genres());
        model.addAttribute("series",seriesRepo.findAll());
        return "fragments/book-form";
    }*/
    @GetMapping("/sortFragment/{attribute}/{ascending}")
    public String sortByAttribute(@PathVariable String attribute, @PathVariable Boolean ascending, Model model){
        model.addAttribute("bookList", ascending ? bookRepo.findAll(Sort.by(attribute).ascending()) : bookRepo.findAll(Sort.by(attribute).descending()));
        return "fragments/book-table";
    }

    @PostMapping("/add-book")
    public String createBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model, @RequestParam ("pictureUpload") MultipartFile file, @RequestParam("seriesName")String seriesName){
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();
                book.setPictureFile(bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String input = seriesName.trim();
        Series series = seriesRepo.findBySeriesName(input);
        if(series != null){
            book.setSeries(series);
        }else{
            Series newSeries = new Series(input);
            book.setSeries(newSeries);
            seriesRepo.save(newSeries);
        }
        if(bookRepo.existsById(book.getISBN())){
            bindingResult.rejectValue("ISBN", "error.book", "ISBN already exists");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("bookList", bookRepo.findAll());
            return "fragments/book-form/add-book-form";
        }

        bookRepo.save(book);
        model.addAttribute("bookList", bookRepo.findAll());
        model.addAttribute("book", new Book());
        model.addAttribute("genres", genres());
        model.addAttribute("series",seriesRepo.findAll());
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
        model.addAttribute("series", seriesRepo.findAll());
        model.addAttribute("book", book);
        model.addAttribute("genres", genres());
        return "fragments/book-form/edit-book-form";
    }

    @PostMapping("/update-book")
    public String updateBook(@ModelAttribute Book book, @RequestParam ("pictureUpload") MultipartFile file, @RequestParam("seriesName")String seriesName){
        if(!file.isEmpty()){
            try{
                byte[] bytes = file.getBytes();
                book.setPictureFile(bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String input = seriesName.trim();
        Series series = seriesRepo.findBySeriesName(input);
        if(series != null){
            book.setSeries(series);
        }else{
            Series newSeries = new Series(input);
            book.setSeries(newSeries);
            seriesRepo.save(newSeries);
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
    @PostMapping("/book/{ISBN}/review")
    public String reviewBook(@PathVariable int ISBN, @RequestParam("reviewLevel") int reviewLevel, @RequestParam("review") String review, Model model,HttpSession session){
        Book book = bookRepo.findByISBN(ISBN);
        List<Rating> ratings = book.getRatings();
        Rating rating = null;
        for(int i = 0; i < ratings.size(); i++){
            if(reviewLevel == ratings.get(i).getRatingLevel()){
                rating = ratings.get(i);
            }
        }
        if(rating != null){
            rating.addReview(review);
            model.addAttribute("book", book);
            ShoppingCartController.addShoppingCartAttributes(model, session);
            return "book";
        }
        return  "error/book-not-found";

    }


}
