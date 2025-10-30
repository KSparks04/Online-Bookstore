package org.project.repository;

import org.project.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book,Integer> {
    public Book findBookByISBN(int ISBN);
    public List<Book> findBooksByTitle(String title);
    public List<Book> findBooksByAuthor(String author);
}