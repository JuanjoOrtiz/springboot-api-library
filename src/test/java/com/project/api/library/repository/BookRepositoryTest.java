package com.project.api.library.repository;

import com.project.api.library.entity.Book;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AllArgsConstructor
class BookRepositoryTest {

    private final BookRepository bookRepository;
    private final TestEntityManager testEntityManager;


    @BeforeEach
    void setUp() {
        Book book =
                Book.builder()
                        .isbn("9788466363402")
                        .title("Dune")

                        .build();
    }

    @Test
    void findAll() {
    }

    @Test
    void findByTitle() {
    }
}