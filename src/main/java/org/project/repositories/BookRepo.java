package org.project.repositories;

import java.util.List;
import java.util.Optional;

import org.project.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepo extends CrudRepository<Book,Integer> {
    public Book findBookById(int id);
    public List<Book> findBooksByTitle(String title);
    public List<Book> findBooksByAuthor(String author);
    Optional<Book> findByISBN(String ISBN);
    void deleteByISBN(String ISBN);
}
