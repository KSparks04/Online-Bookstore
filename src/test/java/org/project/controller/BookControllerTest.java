package org.project.controller;

import org.junit.jupiter.api.Test;
import org.project.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

	@Autowired
	private MockMvc	mockMvc;

	/**
	 * Tests the get book list function of the book list
	 * @throws Exception
	 */
	@Test
	void getBookList() throws Exception {
		this.mockMvc.perform(get("/get-book-list")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Book List")));
	}

	/**
	 * Tests the search function of the book list
	 * @throws Exception
	 */
	@Test
	void getBookListSearch() throws Exception {
		Book book = new Book(1, "Title 1", "Author 1", "Publisher 1", "Description 1",15,25.99);
		this.mockMvc.perform(post("/add-book").flashAttr("book", book));
		this.mockMvc.perform(get("/get-book-list?function=search&variable=1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("1"))).andExpect(content().string(containsString("Title 1")))
				.andExpect(content().string(containsString("Author 1"))).andExpect(content().string(containsString("Publisher 1")))
				.andExpect(content().string(containsString("Description 1"))).andExpect(content().string(containsString("15"))).andExpect(content().string(containsString("25.99")));
	}

}