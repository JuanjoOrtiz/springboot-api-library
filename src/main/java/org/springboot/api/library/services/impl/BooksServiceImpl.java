package org.springboot.api.library.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.entities.Book;
import org.springboot.api.library.exceptions.NotFoundException;
import org.springboot.api.library.exceptions.ServiceException;
import org.springboot.api.library.mapper.BookMapper;
import org.springboot.api.library.repositories.BookRepository;
import org.springboot.api.library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BookRepository bookRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        try {
            log.info("Retrieving all books with pageable: {}", pageable);
            return bookRepository.findAll(pageable).map(BookMapper.INSTANCE::toDTO);
        } catch (Exception e) {
            log.error("Unexpected error retrieving all books", e);
            throw new ServiceException("Error retrieving all books", e);
        }


    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDTO> getBookById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Book ID must be a positive number");
        }
        log.info("Searching book by id: {}", id);
        try {
            return bookRepository.findById(id)
                    .map(BookMapper.INSTANCE::toDTO);
        } catch (Exception e) {
            log.error("Error retrieving book with ID: {}", id, e);
            throw new ServiceException(STR."Error retrieving book with ID: \{id}", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDTO> getBookByTitleOrISBN(String searchTerm) {
        log.info("Searching book by title or ISBN: {}", searchTerm);
        try {
            return bookRepository.findByTitleOrIsbnContaining(searchTerm)
                    .map(BookMapper.INSTANCE::toDTO);
        }catch (Exception e){
            throw  new ServiceException(STR."Error retrieving book with Title or ISBN\{searchTerm}", e);
        }

    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        validateBookDTO(bookDTO);
        try {
            log.info("Creating new book: {}", bookDTO);
            Function<BookDTO, Book> toEntity = BookMapper.INSTANCE::toEntity;
            Function<Book, BookDTO> toDto = BookMapper.INSTANCE::toDTO;

            return Optional.of(bookDTO)
                    .map(toEntity)
                    .map(bookRepository::save)
                    .map(toDto)
                    .orElseThrow(() -> new ServiceException("Error in book creation pipeline"));
        } catch (Exception e) {
            log.error("Error creating book: {}", bookDTO, e);
            throw new ServiceException("Error creating book", e);
        }
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        log.info("Updating book with ID: {} with data: {}", id, bookDTO);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Book ID must be a positive number");
        }
        validateBookDTO(bookDTO);

        // Lambda for updating book fields
        Consumer<Book> updateFields = book -> {
            book.setTitle(bookDTO.title());
            book.setAuthor(bookDTO.author());
            book.setIsbn(bookDTO.isbn());
            book.setPublisher(bookDTO.publisher());
            book.setPublication_year(bookDTO.publicationYear());
            book.setCategory(bookDTO.category());
            book.setAvailable_quantity(bookDTO.availableQuantity());
            book.setRoute_image(bookDTO.routeImage());
        };

        return bookRepository.findById(id)
                .map(book -> {
                    try {
                        updateFields.accept(book);
                        return bookRepository.save(book);
                    } catch (Exception e) {
                        log.error("Error updating book entity with ID: {}", id, e);
                        throw new ServiceException(STR."Error updating book with ID: \{id}", e);
                    }
                })
                .map(BookMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new NotFoundException(STR."Book not found with ID: \{id}"));
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Book ID must be a positive number");
        }
        try {
            bookRepository.findById(id)
                    .ifPresentOrElse(
                            book -> bookRepository.deleteById(id),
                            () -> { throw new NotFoundException(STR."Book not found with ID: \{id}"); }
                    );
        } catch (Exception e) {
            log.info("Successfully deleted book with ID: {}", id);
            throw new ServiceException(STR."Error deleting book with ID: \{id}", e);
        }
    }


private void validateBookDTO(BookDTO bookDTO) {
    log.debug("Validating BookDTO: {}", bookDTO);
    Optional.ofNullable(bookDTO)
            .orElseThrow(() -> {
                log.warn("Null BookDTO provided");
                return new IllegalArgumentException("BookDTO cannot be null");
            });

    Optional.ofNullable(bookDTO.title())
            .filter(title -> !title.trim().isEmpty())
            .orElseThrow(() -> {
                log.warn("Invalid title in BookDTO: {}", bookDTO);
                return new IllegalArgumentException("Book title cannot be null or empty");
            });

    Optional.ofNullable(bookDTO.author())
            .filter(author -> !author.trim().isEmpty())
            .orElseThrow(() -> {
                log.warn("Invalid author in BookDTO: {}", bookDTO);
                return new IllegalArgumentException("Book author cannot be null or empty");
            });

    Optional.ofNullable(bookDTO.isbn())
            .filter(isbn -> !isbn.trim().isEmpty())
            .orElseThrow(() -> {
                log.warn("Invalid ISBN in BookDTO: {}", bookDTO);
                return new IllegalArgumentException("Book ISBN cannot be null or empty");
            });
    log.debug("BookDTO validation successful");
}
}
