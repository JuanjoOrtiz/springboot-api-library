package com.springboot.api.library.controllers;


import com.springboot.api.library.dtos.BookRequestDTO;
import com.springboot.api.library.dtos.BookResponseDTO;
import com.springboot.api.library.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping
    public ResponseEntity<Page<?>> findAllBooks(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        try {
            log.info("Fetching all books with pagination: page={}, size={}",
                    pageable.getPageNumber(), pageable.getPageSize());

            Page<BookResponseDTO> books = bookService.findAll(pageable);
            if (books.isEmpty()) {
                log.info("Books not found");
                return ResponseEntity.noContent().build();
            }
            log.info("Books found: {}", books.getTotalElements());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            log.error("Error occurred while fetching books", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBookById(@PathVariable Long id) {
        try {
            log.info("Fetching Book by Id: {}", id);

            return bookService.findById(id)
                    .map(book -> {
                        log.info("Book found with id: {}", id);
                        return ResponseEntity.ok(book);
                    })
                    .orElseGet(() -> {
                        log.info("Book not found with id: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error occurred while fetching book with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookRequestDTO bookRequest) {
        try {
            log.info("Creating new book: {}", bookRequest);
            BookResponseDTO createdBook = bookService.create(bookRequest);
            log.info("Book created successfully with id: {}", createdBook.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
        } catch (Exception e) {
            log.error("Error occurred while creating book", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO bookRequestDto) {
        try {
            log.info("Updating book with id: {}", id);
            BookResponseDTO updatedBook = bookService.update(bookRequestDto, id);
            log.info("Book updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedBook);
        } catch (EntityNotFoundException e) {
            log.warn("Book not found for update with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while updating book with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            log.info("Deleting book with id: {}", id);
            if (bookService.delete(id)) {
                log.info("Book deleted successfully with id: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                log.info("Book not found for deletion with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting book with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
