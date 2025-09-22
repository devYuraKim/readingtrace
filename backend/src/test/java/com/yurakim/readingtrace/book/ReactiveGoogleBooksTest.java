package com.yurakim.readingtrace.book;

import com.yurakim.readingtrace.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReactiveGoogleBooksTest {

    @Autowired
    private BookService bookService;

    @Test
    void testSearchBooks() {
        bookService.reactiveSearchBook("title", "the divine comedy")
                .doOnNext(book -> System.out.println(book.getTitle() + " | " + book.getBookId()))
                .blockLast();
    }

    @Test
    void testSearchBooks_allChecks() {
        List<String> searchTypes = List.of("title", "author", "isbn");
        String searchWord = "the divine comedy";

        for (String type : searchTypes) {
            bookService.reactiveSearchBook(type, searchWord)
                    .doOnNext(book -> {
                        assertNotNull(book.getBookId(), "Book ID should not be null");
                        assertNotNull(book.getTitle(), "Title should not be null");
                        assertFalse(book.getTitle().isBlank(), "Title should not be blank");

                        assertNotNull(book.getAuthors(), "Authors should not be null");
                        assertNotNull(book.getDescription(), "Description should not be null");

                        assertNotNull(book.getImageLinks(), "Thumbnail should not be null");
                        assertTrue(book.getImageLinks().isEmpty() || book.getImageLinks().startsWith("http"));

                        assertNotNull(book.getIsbn10(), "ISBN10 should not be null");
                        assertNotNull(book.getIsbn13(), "ISBN13 should not be null");

                        System.out.printf("[%s] %s | %s | Authors: %s%n",
                                type, book.getTitle(), book.getBookId(), book.getAuthors());
                    })
                    .blockLast(); // block only in tests to consume the Flux
        }
    }

}
