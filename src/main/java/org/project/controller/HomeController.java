package org.project.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpSession;
import org.project.model.Book;
import org.project.model.Rating;
import org.project.model.Series;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.project.repository.SeriesRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

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

    public void setup() {
        readCSV();
        readUsersCSV();
        readReviewCSV();
        Series series1 = new Series("Harry Potter");
        Series series2 = new Series("Divergent");
        Series series3 = new Series("James Bond");

        if (seriesRepository.findBySeriesName(series1.getSeriesName()) == null) seriesRepository.save(series1);
        if (seriesRepository.findBySeriesName(series2.getSeriesName()) == null) seriesRepository.save(series2);
        if (seriesRepository.findBySeriesName(series3.getSeriesName()) == null) seriesRepository.save(series3);

        User admin = new User("admin", "admin", true);
        userRepository.save(admin);
    }

    public void readCSV() {
        String line;
        String delimit = ",";
        try (InputStream is = getClass().getResourceAsStream("/static/Database/books.csv")) {
            if (is == null) {
                return;
            }
            CSVReader br = new CSVReader(new InputStreamReader(is));
            String[] lineArr = br.readNext(); //Skip first line
            while ((lineArr = br.readNext()) != null) {
                //System.out.println(Arrays.toString(lineArr));

                Book book = new Book();
                book.setISBN(Long.parseLong(lineArr[0]));
                book.setTitle(lineArr[1]);
                book.setAuthor(lineArr[2]);
                book.setPublisher(lineArr[3]);
                String[] genre = lineArr[4].split("/");
                for (String gen : genre) {
                    book.addGenre(gen);
                }
                if (!lineArr[5].isEmpty()) {
                    Series series = seriesRepository.findBySeriesName(lineArr[5]);
                    if (series != null) {
                        book.setSeries(series);
                    } else {
                        Series newSeries = new Series(lineArr[5]);
                        book.setSeries(newSeries);
                        seriesRepository.save(newSeries);
                    }
                } else {
                    book.setSeries(null);
                }

                book.setPrice(Double.parseDouble(lineArr[6]));
                book.setInventory(Integer.parseInt(lineArr[7]));
                book.setPageCount(Integer.parseInt(lineArr[8]));

                book.setDescription(lineArr[9]);
                book.setBookType(lineArr[10]);
                byte[] imageBytes = null;
                String filepath;
                if(lineArr[11].isEmpty()){
                    filepath = "default_image.jpg";
                }else{
                    filepath = lineArr[11];
                }
                try (InputStream imageIn = getClass().getResourceAsStream("/static/images/book-covers/" + filepath)) {
                    if (imageIn != null) {
                        imageBytes = imageIn.readAllBytes();
                        book.setPictureFile(imageBytes);

                    }




                } catch (IOException e) {
                    e.printStackTrace();
                }


                bookRepository.save(book);
            }
//ISBN	Title	Author	Publisher	Genres	Series	Price	Inventory	Page Count	Description	Cover type
        } catch (IOException e) {
            return;
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

    }

    public void readReviewCSV() {
        try (InputStream is = getClass().getResourceAsStream("/static/Database/reviews.csv")) {
            if (is == null) {
                System.out.println("reviews.csv not found");
                return;
            }

            CSVReader br = new CSVReader(new InputStreamReader(is));
            String[] lineArr = br.readNext(); // skip first line
            while ((lineArr = br.readNext()) != null) {

                long isbn = Long.parseLong(lineArr[0].trim());
                Book book = bookRepository.findByISBN(isbn);

                String username = lineArr[1].trim();
                User user = userRepository.findByUsername(username);

                int ratingInt;
                ratingInt = Integer.parseInt(lineArr[2].trim());

                Rating.Level level = Rating.Level.fromInt(ratingInt);

                String review = lineArr[3].trim();

                Rating rating = new Rating(book, user, level, review, LocalDateTime.now());
                book.getRatings().add(rating);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading reviews CSV", e);
        }
    }

    public void readUsersCSV() {
        try (InputStream is = getClass().getResourceAsStream("/static/Database/users.csv")) {
            if (is == null) {
                System.out.println("users.csv not found in resources");
                return;
            }

            CSVReader br = new CSVReader(new InputStreamReader(is));
            String[] lineArr = br.readNext(); // skip first line
            while ((lineArr = br.readNext()) != null) {
                String username = lineArr[0].trim().toLowerCase();
                String password = lineArr[1].trim();
                boolean isOwner = Boolean.parseBoolean(lineArr[2].trim());

                // Check if user already exists
                User user = userRepository.findByUsername(username);
                if (user == null) {
                    user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setIsOwner(isOwner);
                    userRepository.save(user);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Error reading users CSV", e);
        }
    }
    public List<Book> loadReviewWidget(){
        List<Book> books = bookRepository.findAll();
        HashMap<Double, Book> bookHashMap = new HashMap<>();
        for(Book book : books){
            bookHashMap.put(book.averageRating(), book);
        }
        List<Book> popularBooks = new ArrayList<>();
        for(Map.Entry<Double, Book> entry : bookHashMap.entrySet()){
            if(entry.getKey() >= 3.5){
                popularBooks.add(entry.getValue());
            }
        }
        return popularBooks;
    }
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<Book> popularBooks = bookRepository.findAll();
        if (setup) {
            setup = false;
            setup();
            popularBooks = loadReviewWidget();
        }
        model.addAttribute("popularBooks",popularBooks);
        ShoppingCartController.addShoppingCartAttributes(model, session);
        return "home";
    }

}
