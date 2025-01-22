package com.postgres.bookshop.controllers;

import com.postgres.bookshop.models.Author;
import com.postgres.bookshop.models.Book;
import com.postgres.bookshop.repository.AuthorRepository;
import com.postgres.bookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        // om author fylls i - kolla att den finns i db
        if(book.getAuthor() != null && !authorRepository.existsById(book.getAuthor().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author not found");
        }

        // om coAuthor fylls i - kolla att den finns i db
        if(book.getCoAuthor() != null && !authorRepository.existsById(book.getCoAuthor().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CoAuthor not found");
        }

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found."));

        if (book.getTitle() != null) {
            existingBook.setTitle(book.getTitle());
        }

        if (book.getPages() != null) {
            existingBook.setPages(book.getPages());
        }

        if (book.getDescription() != null) {
            existingBook.setDescription(book.getDescription());
        }

        if (book.getAuthor() != null) {
            Author author = authorRepository.findById(book.getAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Author not found"
                    ));
            existingBook.setAuthor(author);
        }

        if (book.getCoAuthor() != null) {
            Author coAuthor = authorRepository.findById(book.getCoAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "CoAuthor not found"
                    ));
            existingBook.setCoAuthor(coAuthor);
        }

        if (book.getPriceExVat() != null) {
            existingBook.setPriceExVat(book.getPriceExVat());
        }

        if (book.getIsbn() != null) {
            existingBook.setIsbn(book.getIsbn());
        }

        if (book.getBookCoverUrl() != null) {
            existingBook.setBookCoverUrl(book.getBookCoverUrl());
        }

        Book updatedBook = bookRepository.save(existingBook);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if(!bookRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

