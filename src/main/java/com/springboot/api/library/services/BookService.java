package com.springboot.api.library.services;

import com.springboot.api.library.dtos.BookDTO;
import com.springboot.api.library.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookDTO> findAll();
    Optional<BookDTO> findById(Long id);
    Book create(BookDTO bookDTO);
    Optional<Book> update(BookDTO bookDTO, Long id);
    void delete(Long id);

}
