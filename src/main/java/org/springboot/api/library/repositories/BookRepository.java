package org.springboot.api.library.repositories;

import org.springboot.api.library.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE CONCAT('%', :searchTerm, '%') " +
            "OR b.isbn LIKE CONCAT('%', :searchTerm, '%')")
    Optional<Book> findByTitleOrIsbnContaining(String searchTerm);
}
