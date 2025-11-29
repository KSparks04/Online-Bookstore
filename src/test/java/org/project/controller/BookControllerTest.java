package org.project.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.project.model.Book;
import org.project.model.User;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the get book list function of the book list
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void getBookList() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        this.mockMvc.perform(get("/get-book-list").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Book List")));
    }

    /**
     * Tests the search function of the book list
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void getBookListSearch() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        //Book book = new Book(1, "Title 1", "Author 1", "Publisher 1", "Description 1",15,25.99);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());
        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN", "101").param("title", "Title 1").param("author", "Author 1")
                .param("publisher", "Publisher 1").param("description", "Description 1").param("inventory", "15").param("price", "25.99").param("pageCount", "101").param("seriesName", "Divergent").session(session)).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(get("/get-book-list?function=search&variable=101").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("101")))
                .andExpect(content().string(containsString("Title 1")))
                .andExpect(content().string(containsString("Author 1")))
                .andExpect(content().string(containsString("Publisher 1")))
                .andExpect(content().string(containsString("Description 1")))
                .andExpect(content().string(containsString("15")))
                .andExpect(content().string(containsString("25.99")));
    }

    /**
     * Adds a sample book
     * Calls /book/{id}
     * Confirms all book info appears on the resulting page
     */
    @Test
    @Transactional
    void getBookDetails() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        Book book = new Book(123, "Sample Title", "Author Name", "Publisher Co", "Sample description", 1, 1.0, 5);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());
        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN", "123").param("title", "Sample Title").param("author", "Author Name")
                .param("publisher", "Publisher Co").param("description", "Sample description").param("inventory", "1").param("price", "1.0").param("pageCount", "5").param("seriesName","Divergent").session(session)).andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/book/123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sample Title")))
                .andExpect(content().string(containsString("Author Name")))
                .andExpect(content().string(containsString("Publisher Co")))
                .andExpect(content().string(containsString("Sample description")))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("1.0")));
        ;

    }

    /**
     * Requests a non-existing book ID.
     * Verifies that the response is the friendly “Book Not Found” page.
     */
    @Test
    @Transactional
    void getBookNotFound() throws Exception {
        this.mockMvc.perform(get("/book/99999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Book Not Found")))
                .andExpect(content().string(containsString("Return to Book List")));
    }

    @Test
    @Transactional
    void searchBookNotFound() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        this.mockMvc.perform(get("/get-book-list").session(session)
                        .param("function", "search")
                        .param("variable", "NonExistentBook"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("NonExistentBook")));
    }


    /**
     * Tests the create book method
     *
     * @throws Exception
     */
    //Need test for no picture still
    @Test
    @Transactional
    void addBook() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        //Book book = new Book(201, "Title 201", "Author 201", "Publisher 201", "Description 201", 15, 33.95, 4);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());
        this.mockMvc.perform(multipart("/add-book").file(file)
                .param("ISBN", "201")
                .param("title", "Title 201")
                .param("author", "Author 201")
                .param("publisher", "Publisher 201")
                .param("description", "Description 201")
                .param("inventory", "15")
                .param("price", "33.95")
                .param("pageCount", "4")
                .param("seriesName", "Divergent").session(session))
                .andExpect(status().is3xxRedirection());
        this.mockMvc.perform(get("/get-book-list").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("201"))).andExpect(content().string(containsString("Title 201")))
                .andExpect(content().string(containsString("Author 201"))).andExpect(content().string(containsString("Publisher 201")))
                .andExpect(content().string(containsString("Description 201"))).andExpect(content().string(containsString("15"))).andExpect(content().string(containsString("33.95")));

    }

    /**
     * Tests the delete book method
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void deleteBook() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        Book book = new Book(7, "Title 7", "Author 7", "Publisher 7", "Description 7", 13, 26.99, 3);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());
        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN", "7").param("title", "Title 7").param("author", "Author 7")
                .param("publisher", "Publisher 7").param("description", "Description 7").param("inventory", "13").param("price", "26.99").param("pageCount", "3").param("seriesName","Divergent").session(session)).andExpect(status().is3xxRedirection());
        //this.mockMvc.perform(post("/add-book").flashAttr("book", book));
        this.mockMvc.perform(post("/delete-book/7").session(session)).andDo(print()).andExpect(status().is3xxRedirection());
        //Might have to change but a 7 can appear for something else in the html so .andExpect(content().string(not(containString("7"))) won't work for checking ISBN
        this.mockMvc.perform(get("/get-book-list").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Title 7"))))
                .andExpect(content().string(not(containsString("Author 7")))).andExpect(content().string(not(containsString("Publisher 7"))))
                .andExpect(content().string(not(containsString("Description 7"))));//removed checks for price and inventory as other books could have these values

    }

    /**
     * Tests the edit book method
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void editBook() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        Book book = new Book(301, "Title 301", "Author 301", "Publisher 301", "Description 301", 15, 33.95, 2);

        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());
        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN", "301").param("title", "Title 301").param("author", "Author 301")
                .param("publisher", "Publisher 301").param("description", "Description 301").param("inventory", "15").param("price", "33.95").param("pageCount", "2").param("seriesName","Divergent").session(session)).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(get("/edit-book/301?").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("15"))).andExpect(content().string(containsString("33.95")));
    }

    /**
     * Tests the update book method
     *
     * @throws Exception
     */
    @Test
    @Transactional
    void updateBook() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        // Has to be changed to 5 (anything not 401) as to not conflict with deleteBook test.
        Book book = new Book(401, "Title 401", "Author 401", "Publisher 401", "Description 401", 15, 29.99, 1);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());

        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN", "401").param("title", "Title 401").param("author", "Author 401")
                .param("publisher", "Publisher 401").param("description", "Description 401").param("inventory", "15").param("price", "29.99").param("pageCount", "1").param("seriesName","Divergent").session(session)).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(multipart("/update-book").file(file)
                        .param("ISBN", "401")
                        .param("title", "New Title 401")
                        .param("author", "New Author 401")
                        .param("publisher", "New Publisher 401")
                        .param("description", "New Description 401")
                        .param("inventory", "20")
                        .param("price", "39.99")
                        .param("pageCount", "1")
                        .param("seriesName", "Divergent").session(session))
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/get-book-list").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("401"))).andExpect(content().string(containsString("New Title 401")))
                .andExpect(content().string(containsString("Author 401"))).andExpect(content().string(containsString("New Publisher 401")))
                .andExpect(content().string(containsString("New Description 401"))).andExpect(content().string(containsString("20"))).andExpect(content().string(containsString("39.99")));

    }

}