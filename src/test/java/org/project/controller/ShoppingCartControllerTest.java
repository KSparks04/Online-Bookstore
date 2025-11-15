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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the get shopping cart method
     * @throws Exception
     */
    @Test
    void getShoppingCart() throws Exception {
        MockHttpSession session = null;
        Book book = new Book(201, "Title 201", "Author 201", "Publisher 201", "Description 201", 15, 33.95, 4);
        this.mockMvc.perform(multipart("/add-book").param("ISBN","201").param("title","Title 201").param("author","Author 201")
                .param("publisher","Publisher 201").param("description","Description 201").param("inventory","15").param("price","33.95").param("pageCount", "4").param("seriesName","Divergent"));
        this.mockMvc.perform(get("/shopping-cart")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("No Books in Shopping Cart")));
    }

    /**
     * Tests the edit shopping cart method
     * @throws Exception
     */
    @Test
    void editShoppingCart() throws Exception {
        MockHttpSession session = null;
        Book book = new Book(4000, "Sample Title", "Author Name", "Publisher Co", "Sample description", 1, 1.0, 5);
        MockMultipartFile file = new MockMultipartFile("pictureUpload", "file.jpeg", "image/jpeg", "Hello World".getBytes());

        this.mockMvc.perform(multipart("/add-book").file(file).param("ISBN","4000").param("title","Sample Title").param("author","Author Name")
                .param("publisher","Publisher Co").param("description","Sample description").param("inventory","1").param("price","1.0").param("pageCount", "5").param("seriesName","Not Divergent")).andExpect(status().is3xxRedirection());

        //Test add
        session = (MockHttpSession) this.mockMvc.perform(post("/shopping-cart/edit/add/4000")).andDo(print()).andReturn().getRequest().getSession();
        session = (MockHttpSession) this.mockMvc.perform(get("/shopping-cart").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Sample Title"))).andReturn().getRequest().getSession();
        //Test remove
        session = (MockHttpSession) this.mockMvc.perform(post("/shopping-cart/edit/remove/4000")).andDo(print()).andReturn().getRequest().getSession();
        this.mockMvc.perform(get("/shopping-cart").session(session)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("No Books in Shopping Cart")));
    }
    @Test
    void checkoutWithoutLoginShowsError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        ShoppingCart cart = new ShoppingCart();
        cart.addBook(new Book(1, "Sample Book", "Author", "Publisher", "Desc", 12, 1.3, 1));
        session.setAttribute("shoppingCart", cart);

        mockMvc.perform(post("/shopping-cart/validate-checkout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    void checkoutWithUserCreatesPurchases() throws Exception {
        Book book = bookRepository.save(new Book(1234, "Checkout Test", "Author", "Pub", "Desc", 1, 1.0, 23));

        MockHttpSession session = new MockHttpSession();
        User user = userRepository.save(new User("buyer", "pass"));
        session.setAttribute("currentUser", user);

        ShoppingCart cart = new ShoppingCart();
        cart.addBook(book);
        session.setAttribute("shoppingCart", cart);

        mockMvc.perform(post("/shopping-cart/checkout-success").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Thank you for your purchase!")));
    }
}