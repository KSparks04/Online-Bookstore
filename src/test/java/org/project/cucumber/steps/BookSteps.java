package org.project.cucumber.steps;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import jakarta.transaction.Transactional;
import org.project.model.User;
import org.project.repository.SeriesRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import org.project.model.Book;
import org.project.repository.BookRepository;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BookSteps {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SeriesRepository seriesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    MockMultipartHttpServletRequestBuilder builder;
    Map<String, String> data = new HashMap<>();

    @Given("I am on the Add Book Form")
    public void i_am_on_the_add_book_form() {
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }
    @When("I submit a book with:")
    @Transactional
    public void i_submit_a_book_with(DataTable dataTable) throws  Exception {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        for (Map<String, String> row : rows) {
            for (Map.Entry<String, String> e : row.entrySet()) {
                data.put(e.getKey().trim(), e.getValue().trim());
            }
        }

        MockMultipartFile imageFile = null;
        if(data.containsKey("ImageFile")){
            Path path = Paths.get("src/test/resources/images/"+data.get("ImageFile"));
            imageFile = new MockMultipartFile("pictureUpload",data.get("ImageFile"),"image/jpeg", Files.readAllBytes(path));
        }else {
            imageFile = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());
        }


        this.mockMvc.perform(multipart("/add-book")
                        .file(imageFile)
                        .param("ISBN", data.get("ISBN"))
                        .param("title", data.get("Title"))
                        .param("author", data.get("Author"))
                        .param("publisher", data.get("Publisher"))
                        .param("description", data.get("Description"))
                        .param("inventory", data.get("Inventory"))
                        .param("price", data.get("Price"))
                        .param("pageCount", data.get("PageCount"))
                        .param("seriesName",data.get("Series")).session(session))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        userRepository.removeByIsOwner(true);
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        /**
         *  this.mockMvc.perform(multipart("/add-book")
         *                 .file(imageFile)
         *                 .param("ISBN", "1939382938501")
         *                 .param("title", "The Maze Runner")
         *                 .param("author", "James Dashner")
         *                 .param("publisher", "Bloomsbury")
         *                 .param("description", "They have to escape")
         *                 .param("inventory", "25")
         *                 .param("price", "18.99")
         *                 .param("pageCount", "350")
         *                 .param("seriesName","Maze Runner")).andExpect(status().is3xxRedirection());
         */

    }
    @Then("the book should be saved successfully")
    public void the_book_should_be_saved_successfully() {
        // Write code here that turns the phrase above into concrete actions
        Book book = bookRepository.findByISBN(Integer.parseInt(data.get("ISBN")));
        assertEquals(Integer.parseInt(data.get("ISBN")),book.getISBN());
    }
    @Then("I should see it on the Book List")
    @Transactional
    public void i_should_see_it_on_the_book_list() throws Exception {

        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        // Write code here that turns the phrase above into concrete actions
        this.mockMvc.perform(get("/get-book-list?function=search&variable=19393501").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(data.get("ISBN"))))
                .andExpect(content().string(containsString(data.get("Title"))))
                .andExpect(content().string(containsString(data.get("Author"))))
                .andExpect(content().string(containsString(data.get("Publisher"))))
                .andExpect(content().string(containsString(data.get("Description"))))
                .andExpect(content().string(containsString(data.get("Inventory"))))
                .andExpect(content().string(containsString(data.get("Price"))));

        userRepository.removeByIsOwner(true);
    }
}
