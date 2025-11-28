package org.project.controller;

import org.junit.jupiter.api.Test;
import org.project.model.Book;
import org.project.model.ShoppingCart;
import org.project.model.User;
import org.project.repository.BookRepository;
import org.project.repository.PurchaseRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.transaction.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the get wishlist method
     * @throws Exception
     */
    @Test
    @Transactional
    void getWishlist() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("buyer", "pass");
        session.setAttribute("currentUser", user);

        this.mockMvc.perform(get("/wishlist").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("No Books in Shopping Cart")));
    }

    /**
     * Tests the edit wishlist method
     * @throws Exception
     */
    @Test
    @Transactional
    void editWishlist() throws Exception {
        MockHttpSession session = new MockHttpSession();
        User user = new User("tester", "password", true);
        userRepository.save(user);
        session.setAttribute("currentUser", user);

        Book book = new Book(4100, "Sample Title", "Author Name", "Publisher Co", "Sample description", 1, 1.0, 5);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());

        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN","4100").param("title","Sample Title").param("author","Author Name")
                .param("publisher","Publisher Co").param("description","Sample description").param("inventory","1").param("price","1.0").param("pageCount", "5").param("seriesName","Not Divergent").session(session)).andExpect(status().is3xxRedirection());

        //Test add
        session = (MockHttpSession) this.mockMvc.perform(post("/wishlist/edit/add/4100").session(session)).andDo(print()).andReturn().getRequest().getSession();
        session = (MockHttpSession) this.mockMvc.perform(get("/wishlist").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Sample Title"))).andReturn().getRequest().getSession();
        //Test remove
        session = (MockHttpSession) this.mockMvc.perform(post("/wishlist/edit/remove/4100").session(session)).andDo(print()).andReturn().getRequest().getSession();
        this.mockMvc.perform(get("/wishlist").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("No Books in Shopping Cart")));
    }
}