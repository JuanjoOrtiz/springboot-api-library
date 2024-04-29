package com.project.api.library.repository;

import com.project.api.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Long> {
}
