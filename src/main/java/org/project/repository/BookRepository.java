package org.project.repository;

import java.util.List;

import org.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    public Book findByISBN(long ISBN);
    public List<Book> findByTitle(String title);
    public List<Book> findByAuthor(String author);
    public List<Book> findByPublisher(String publisher);
    public List<Book> findByDescription(String description);
    public boolean existsByISBN(long ISBN);

    @Query("SELECT b FROM Book b " +
            "WHERE LOWER(b.title) LIKE %:str% " +
            "OR LOWER(b.author) LIKE %:str% " +
            "OR LOWER(b.publisher) LIKE %:str% " +
            "OR LOWER(b.description) LIKE %:str% " +
            "OR CONCAT('', b.ISBN) LIKE %:str%")
    public Iterable<Book> findByAllColumns(@Param("str") String str);

     public void deleteByISBN(Long ISBN);
}