package org.springboot.api.library.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.exceptions.*;
import org.springboot.api.library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/v1/books")
public class BooksController {

    @Autowired
    private BooksService booksService;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(@PageableDefault(size = 20, sort = "id") Pageable pageable) throws InvalidPaginationException {
        try {
            log.info("Fetching all books with pageable: {}", pageable);
            Page<BookDTO> books = booksService.getAllBooks(pageable);
            if (books.isEmpty()) {
                log.warn("No books found in the system");
                throw new NotFoundException("No books found in the system");
            }
            log.info("Successfully retrieved {} books", books.getTotalElements());
            return ResponseEntity.ok(books);
        } catch (IllegalArgumentException e) {
            log.error("Invalid pagination parameters: {}", e.getMessage());
            throw new InvalidPaginationException(STR."Invalid pagination parameters: \{e.getMessage()}");
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving books: {}", e.getMessage());
            throw new RetrievalException(STR."Error retrieving books: \{e.getMessage()}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") @Positive Long id) {
        try {
            log.info("Fetching book with id: {}", id);
            return booksService.getBookById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(STR."Book not found with id: \{id}"));
        } catch (NotFoundException e) {
            log.debug("NotFoundException caught for book id: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error retrieving book with id: {}", id, e);
            throw new RetrievalException(STR."Error retrieving book with id \{id}: \{e.getMessage()}");
        }
    }

    @GetMapping(value = "/searchTerm", params = "searchTerm")
    public ResponseEntity<BookDTO> getBookByTitleOrISBN(@RequestParam("searchTerm") String searchTerm) {
        try {
            log.info("Starting search for book with title or ISBN: {}", searchTerm);
            return booksService.getBookByTitleOrISBN(searchTerm)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(STR."Book not found with title or ISBN: \{searchTerm}"));
        } catch (NotFoundException e) {
            log.debug("NotFoundException caught for search term: {}", searchTerm);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieving book with title or ISBN: {}", searchTerm, e);
            throw new RetrievalException(STR."Error retrieving title or ISBN \{searchTerm}: \{e.getMessage()}");
        }
    }


    @PostMapping
    public BookDTO createBook(@Valid @RequestBody BookDTO bookDTO) {

        try {
            if (bookDTO == null) {
                throw new InvalidDataException("Book data cannot be null");
            }
            log.info("Creating new book: {}", bookDTO);
            BookDTO createdBook = booksService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook).getBody();
        } catch (InvalidDataException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate entry error while creating book: {}", e.getMessage());
            throw new CreationException(STR."Error creating book - duplicate entry: \{e.getMessage()}");
        } catch (Exception e) {
            log.error("Error creating book: {}", e.getMessage());
            throw new CreationException(STR."Error creating book: \{e.getMessage()}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") @Positive Long id, @Valid @RequestBody BookDTO updatedBookDTO) {
        try {
            if (updatedBookDTO == null) {
                throw new InvalidDataException("Book data cannot be null");
            }
            log.info("Updating book with id: {} with data: {}", id, updatedBookDTO);
            BookDTO updated = booksService.updateBook(id, updatedBookDTO);
            if (updated == null) {
                throw new NotFoundException(STR."Book not found with id: \{id}");
            }
            return ResponseEntity.ok(updated);
        } catch (NotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating book with id {}: {}", id, e.getMessage());
            throw new UpdateException(STR."Error updating book with id \{id}: \{e.getMessage()}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") @Positive Long id) {
        try {
            log.info("Deleting book with id: {}", id);
            booksService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            log.error("Book with id {} not found: ",e.getMessage());
            throw new DeletionException(STR."Book with id \{id} not found: \{e.getMessage()}");
        } catch (Exception e) {
            log.error("Error deleting book with id {}: {}", id, e.getMessage());
            throw new DeletionException(STR."Error deleting book with id \{id}: \{e.getMessage()}");
        }
    }
}
