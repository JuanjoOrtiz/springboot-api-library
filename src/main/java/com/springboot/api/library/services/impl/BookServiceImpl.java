package com.springboot.api.library.services.impl;

import com.springboot.api.library.dtos.BookRequestDTO;
import com.springboot.api.library.dtos.BookResponseDTO;
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
       return bookRepository.findAll(pageable).map(bookMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookResponseDTO> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDTO);
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
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {

    }
}
