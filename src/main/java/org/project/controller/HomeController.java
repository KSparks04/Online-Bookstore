package org.project.controller;

import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.Series;
import org.project.repository.BookRepository;
import org.project.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SeriesRepository seriesRepository;


    //TODO temp probably wanna have a DB with persistence
    private boolean setup = true;

    public void setup(){
        Book book1 = new Book(0000000000001, "Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "BloomsBury", "Description of book 1",5,23.99, 350);
        Book book2 = new Book(0000000000002, "Title2", "Author2", "Publisher2", "Description of book 2",4,21.99, 200);
        Book book3 = new Book(0000000000003, "Title3", "Author3", "Publisher3", "Description of book 3",10,22.99, 450);

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        Series series1 = new Series("Harry Potter");
        Series series2 = new Series("Divergent");
        Series series3 = new Series("James Bond");

        if(seriesRepository.findBySeriesName(series1.getSeriesName()) == null) seriesRepository.save(series1);
        if(seriesRepository.findBySeriesName(series2.getSeriesName()) == null) seriesRepository.save(series2);
        if(seriesRepository.findBySeriesName(series3.getSeriesName()) == null) seriesRepository.save(series3);
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        if (setup){
            setup = false;
            setup();
        }
        ShoppingCartController.addShoppingCartAttributes(model, session);
        return "home";
    }
}
