package com.springboot.api.library.services.impl;

import com.springboot.api.library.dtos.BookRequestDTO;
import com.springboot.api.library.dtos.BookResponseDTO;
import com.springboot.api.library.exceptions.ResourceNotFoundException;
import com.springboot.api.library.mappers.BookMapper;
import com.springboot.api.library.repositories.BookRepository;
import com.springboot.api.library.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        log.debug("Fetching all books with pagination: {}", pageable);
       return bookRepository.findAll(pageable)
               .map(bookMapper::toDTO);
    }

    @Override
    public Optional<BookResponseDTO> findById(Long id) {
        log.debug("Searching book by Id: {}", id);
        return bookRepository.findById(id).map(bookMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookResponseDTO> findByIsbn(String isbn) {
        log.debug("Searching book by ISBN: {}", isbn);
        return bookRepository.findByIsbn(isbn).map(bookMapper::toDTO);
    }

    @Override
    @Transactional
    public BookResponseDTO create(BookRequestDTO bookRequestDTO) {
        log.debug("Creating new book with data: {}", bookRequestDTO);

        var book = bookMapper.toEntity(bookRequestDTO);
        book = bookRepository.save(book);

        log.info("Created new book with id: {}", book.getId());
        return bookMapper.toDTO(book);
    }

    @Override
    @Transactional
    public BookResponseDTO update(BookRequestDTO bookRequestDTO, Long id) {
        log.debug("Updating book with id {} with data: {}", id, bookRequestDTO);

        var existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book not found with id: {}", id);
                    return new ResourceNotFoundException("Book not found with id: " + id);
                });

        bookMapper.updateEntityFromDto(bookRequestDTO, existingBook);
        var updatedBook = bookRepository.save(existingBook);

        log.info("Updated book with id: {}", id);
        return bookMapper.toDTO(updatedBook);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        log.debug("Deleting book with id: {}", id);

        if (!bookRepository.existsById(id)) {
            log.error("Attempt to delete non-existent book with id: {}", id);
            throw new RuntimeException("Book not found with id: " + id);
        }

        bookRepository.deleteById(id);
        log.info("Deleted book with id: {}", id);

        return false;
    }
}
