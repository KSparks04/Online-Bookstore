package org.project.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.Series;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.project.repository.SeriesRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Controller
public class HomeController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private UserRepository userRepository;


    //TODO temp probably wanna have a DB with persistence
    private boolean setup = true;

    public void setup(){
        readCSV();
        Series series1 = new Series("Harry Potter");
        Series series2 = new Series("Divergent");
        Series series3 = new Series("James Bond");

        if(seriesRepository.findBySeriesName(series1.getSeriesName()) == null) seriesRepository.save(series1);
        if(seriesRepository.findBySeriesName(series2.getSeriesName()) == null) seriesRepository.save(series2);
        if(seriesRepository.findBySeriesName(series3.getSeriesName()) == null) seriesRepository.save(series3);

        User admin = new User("admin", "admin", true);
        userRepository.save(admin);
    }
    public void readCSV(){
        String line;
        String delimit = ",";
        try(InputStream is = getClass().getResourceAsStream("/static/Database/books.csv")){
            if(is == null){
                return;
            }
            CSVReader br = new CSVReader(new InputStreamReader(is));
            String[] lineArr = br.readNext(); //Skip first line
            while((lineArr = br.readNext()) != null){
                System.out.println(Arrays.toString(lineArr));

                Book book = new Book();
                book.setISBN(Long.parseLong(lineArr[0]));
                book.setTitle(lineArr[1]);
                book.setAuthor(lineArr[2]);
                book.setPublisher(lineArr[3]);
                String[] genre =  lineArr[4].split("/");
                for(String gen:genre){
                    book.addGenre(gen);
                }
                if(!lineArr[5].isEmpty()){
                    Series series = seriesRepository.findBySeriesName(lineArr[5]);
                    if(series != null){
                        book.setSeries(series);
                    }else{
                        Series newSeries = new Series(lineArr[5]);
                        book.setSeries(newSeries);
                        seriesRepository.save(newSeries);
                    }
                }else{
                    book.setSeries(null);
                }

                book.setPrice(Double.parseDouble(lineArr[6]));
                book.setInventory(Integer.parseInt(lineArr[7]));
                book.setPageCount(Integer.parseInt(lineArr[8]));

                book.setDescription(lineArr[9]);
                book.setBookType(lineArr[10]);
                bookRepository.save(book);


            }
//ISBN	Title	Author	Publisher	Genres	Series	Price	Inventory	Page Count	Description	Cover type
        }catch (IOException e){
            return;
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

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
