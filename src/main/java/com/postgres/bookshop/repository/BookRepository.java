package com.postgres.bookshop.repository;

import com.postgres.bookshop.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
