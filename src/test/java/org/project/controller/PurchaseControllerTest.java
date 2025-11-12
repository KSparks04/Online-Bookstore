package org.project.controller;

import org.junit.jupiter.api.Test;
import org.project.model.Book;
import org.project.model.Purchase;
import org.project.model.User;
import org.project.repository.PurchaseRepository;
import org.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void redirectsToLoginIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/purchase-history"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login")));
    }

    @Test
    void showsPurchaseHistoryWhenLoggedIn() throws Exception {
        User user = userRepository.save(new User("buyer", "password"));
        Book book = new Book(1234, "Test Title", "Test Author", "Pub", "Desc",1, 1.0,12);
        purchaseRepository.save(new Purchase(user, book));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("currentUser", user);

        mockMvc.perform(get("/purchase-history").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Title")));
    }
}
