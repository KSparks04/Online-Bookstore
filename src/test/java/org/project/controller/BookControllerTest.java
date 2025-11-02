package org.project.controller;

import org.junit.jupiter.api.Test;
import org.project.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
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
    /**
     * Adds a sample book
     * Calls /book/{id}
     * Confirms all book info appears on the resulting page
     * @throws Exception
     */
    @Test
    void getBookDetails() throws Exception {
        Book book = new Book(123, "Sample Title", "Author Name", "Publisher Co", "Sample description", 1, 1.0);
        this.mockMvc.perform(post("/add-book").flashAttr("book", book));

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
     * Tests the create book method
     * @throws Exception
     */
    @Test
    void addBook() throws Exception {
        Book book = new Book(1, "Title 1", "Author 1", "Publisher 1", "Description 1", 15, 33.95);
        this.mockMvc.perform(post("/add-book").flashAttr("book", book));
        this.mockMvc.perform(get("/get-book-list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("1"))).andExpect(content().string(containsString("Title 1")))
                .andExpect(content().string(containsString("Author 1"))).andExpect(content().string(containsString("Publisher 1")))
                .andExpect(content().string(containsString("Description 1"))).andExpect(content().string(containsString("15"))).andExpect(content().string(containsString("33.95")));

    }

    /**
     * Tests the delete book method
     * @throws Exception
     */
    @Test
    void deleteBook() throws Exception {
        Book book = new Book(7, "Title 7", "Author 7", "Publisher 7", "Description 7", 13, 26.99);
        this.mockMvc.perform(post("/add-book").flashAttr("book", book));
        this.mockMvc.perform(post("/delete-book/7")).andDo(print()).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(get("/get-book-list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("7")))).andExpect(content().string(not(containsString("Title 7"))))
                .andExpect(content().string(not(containsString("Author 7")))).andExpect(content().string(not(containsString("Publisher 7"))))
                .andExpect(content().string(not(containsString("Description 7")))).andExpect(content().string(not(containsString("13")))).andExpect(content().string(not(containsString("26.99"))));

    }

    /**
     * Tests the edit book method
     * @throws Exception
     */
    @Test
    void editBook() throws Exception {
        Book book = new Book(1, "Title 1", "Author 1", "Publisher 1", "Description 1", 15, 33.95);
        this.mockMvc.perform(post("/add-book").flashAttr("book", book));
        this.mockMvc.perform(get("/edit-book/1?")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Edit Book"))).andExpect(content().string(containsString("15"))).andExpect(content().string(containsString("33.95")));
    }

    /**
     * Tests the update book method
     * @throws Exception
     */
    @Test
    void updateBook() throws Exception {
        // Has to be changed to 5 (anything not 1) as to not conflict with deleteBook test.
        Book book = new Book(1, "Title 1", "Author 1", "Publisher 1", "Description 1", 15, 29.99);
        this.mockMvc.perform(post("/add-book").flashAttr("book", book));
        this.mockMvc.perform(post("/update-book")
                .param("title", "New Title 1")
                .param("author", "New Author 1")
                .param("publisher", "New Publisher 1")
                .param("description", "New Description 1"))
                .andExpect(status().is3xxRedirection());

        this.mockMvc.perform(get("/get-book-list")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("1"))).andExpect(content().string(containsString("New Title 1")))
                .andExpect(content().string(containsString("Author 1"))).andExpect(content().string(containsString("New Publisher 1")))
                .andExpect(content().string(containsString("New Description 1"))).andExpect(content().string(containsString("15"))).andExpect(content().string(containsString("29.99")));

    }
}