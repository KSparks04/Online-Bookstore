package org.project.repository;

import org.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findByISBN(long ISBN);
    public List<Book> findByTitle(String title);
    public List<Book> findByAuthor(String author);
    public List<Book> findByPublisher(String publisher);
    public List<Book> findByDescription(String description);

    @Query("SELECT b FROM Book b " +
            "WHERE LOWER(b.title) LIKE %:str% " +
            "OR LOWER(b.author) LIKE %:str% " +
            "OR LOWER(b.publisher) LIKE %:str% " +
            "OR LOWER(b.description) LIKE %:str% " +
            "OR CONCAT('', b.ISBN) LIKE %:str%")
    public Iterable<Book> findByAllColumns(@Param("str") String str);

    @Modifying
    @Transactional
    @Query(     
                "UPDATE Book b " +
                "SET b.inventory = b.inventory - :x " +
                "WHERE b.ISBN = :isbn " +
                "AND b.inventory > 0 "          
        )
    public void decreaseInventoryByISBN(@Param("isbn") long ISBN, @Param("x") int x);
}