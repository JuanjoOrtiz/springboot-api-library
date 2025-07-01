package com.springboot.api.library.services;

import com.springboot.api.library.dtos.BookDTO;
import com.springboot.api.library.entities.Book;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {

    @Override
    public List<BookDTO> findAll() {
        return List.of();
    }

    @Override
    public Optional<BookDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Book create(BookDTO bookDTO) {
        return null;
    }

    @Override
    public Optional<Book> update(BookDTO bookDTO, Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }
}
