package com.yurakim.readingtrace.book.repository;

import com.yurakim.readingtrace.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long bookId);
    Optional<Book> findByExternalId(String externalId);

    @Query("SELECT b.id FROM Book b WHERE b.externalId IN :externalIds")
    Set<Long> findIdsIn(@Param("externalIds") Set<String> externalIds);

    @Query("SELECT b.externalId FROM Book b WHERE b.id IN :bookIds")
    Set<String> findExternalIdsIn(Set<Long> bookIds);

}
