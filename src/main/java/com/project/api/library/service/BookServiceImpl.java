package com.project.api.library.service;


import com.project.api.library.entity.Book;
import com.project.api.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;




    public List<Book> findAll() {
        return bookRepository.findAll();
    }

   /* @Override
    public Optional<BookDTO> findById(Long id) {
        return  bookRepository.findById(id);
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        return bookRepository.save(bookDTO);
    }

   @Override
    public void delete(Long id) {
        bookRepository.delete(id);
    }*/
}
