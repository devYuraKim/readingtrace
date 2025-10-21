package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long bookId);
    Optional<Book> findByExternalId(String externalId);

}
