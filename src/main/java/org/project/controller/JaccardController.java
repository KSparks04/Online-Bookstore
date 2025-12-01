package org.project.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.project.model.Book;
import org.project.model.JaccardEntry;
import org.project.repository.BookRepository;
import org.project.repository.JaccardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class JaccardController {
    
    @Autowired
    private JaccardRepository jaccardRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/similar/{ISBN}")
    @ResponseBody
    public List<JaccardEntry> getSimilarBooks(@PathVariable("ISBN") Long ISBN){
        if(!jaccardRepository.existsByreferenceBookISBN(ISBN)){
            calculateEntries(bookRepository.findByISBN(ISBN));
        }
        return jaccardRepository.findTopSimilarBooks(ISBN, Pageable.ofSize(10));
    }

    @PostMapping("/similar/add/{ISBN}")
    @ResponseBody
    public void addBook(@PathVariable("ISBN")Long ISBN){
        Book refBook = bookRepository.findByISBN(ISBN);
        if(refBook == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        calculateEntries(refBook);
    }


    private List<JaccardEntry> calculateEntries(Book referenceBook){
        List<Book> otherBooks = bookRepository.findByISBNNot(referenceBook.getISBN());

        Set<String> refTags = referenceBook.getTagSet();

        List<JaccardEntry> results = new ArrayList<>();

        for(Book other: otherBooks){
            Set<String> otherTags = other.getTagSet();
            double similarity = jaccard(refTags, otherTags);
            results.add(new JaccardEntry(referenceBook.getISBN(), other.getISBN(), other.getTitle(), similarity));
        }
        jaccardRepository.saveAll(results);
        return results;

    }

    private double jaccard(Set<String> a, Set<String> b){
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);

        Set<String> union = new HashSet<>(a);
        union.addAll(b);

        if(union.isEmpty()) return 0.0;

        return (double) intersection.size()/union.size();
        
    }
}
