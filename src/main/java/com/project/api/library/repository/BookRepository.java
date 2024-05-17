package com.project.api.library.repository;

import com.project.api.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {
 Page<Book> findAll(Pageable pageable);
 Optional<Book> findByTitle(String title);
}
