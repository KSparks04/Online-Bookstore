package org.project.repository;

import org.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO kinda wanna rename this to BookRepository
@Repository
public interface BookRepo extends JpaRepository<Book,Integer> {
    public Book findBookById(int id);
    public List<Book> findBooksByTitle(String title);
    public List<Book> findBooksByAuthor(String author);
}