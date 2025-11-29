package org.project.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import jakarta.transaction.Transactional;
import org.project.model.Book;
import org.project.model.Series;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.project.repository.SeriesRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditBooksSteps {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SeriesRepository seriesRepository;;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    private Book testBook;


    @Given("a book exists with ISBN {string}")
    public void a_book_exists_with_isbn(String string) {
        testBook = new Book(Integer.parseInt(string),"The Maze Runner – Special Edition","James Dashner","Bloomsbury","They have to escape",15,24.50,375);
        Series series = seriesRepository.findBySeriesName("Maze Runner");
        testBook.setSeries(series);

        bookRepository.save(testBook);

        //throw new io.cucumber.java.PendingException();
    }
    @When("I change the book title to {string}")
    public void i_change_the_book_title_to(String string) {
        testBook.setTitle(string);
    }
    @When("I save the book")
    @Transactional
    public void i_save_the_book() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());

        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        this.mockMvc.perform(multipart("/update-book").file(imageFile)
                        .param("ISBN", String.valueOf(testBook.getISBN()))
                        .param("title", testBook.getTitle())
                        .param("author", testBook.getAuthor())
                        .param("publisher", testBook.getPublisher())
                        .param("description", testBook.getDescription())
                        .param("inventory", String.valueOf(testBook.getInventory()))
                        .param("price", String.valueOf(testBook.getPrice()))
                        .param("pageCount", String.valueOf(testBook.getPageCount()))
                        .param("seriesName", testBook.getSeries().getSeriesName()).session(session))
                .andExpect(status().is3xxRedirection());

        userRepository.removeByIsOwner(true);
    }
    @Then("the book title should be updated to {string}")
    public void the_book_title_should_be_updated_to(String string) {
        Book updated = bookRepository.findByISBN(testBook.getISBN());
        assertEquals(string,updated.getTitle());
    }
}
