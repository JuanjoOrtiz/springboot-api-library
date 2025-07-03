package com.springboot.api.library.controllers;


import com.springboot.api.library.dtos.BookResponseDTO;
import com.springboot.api.library.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all Books", description = "Retrieve a paginated list of all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Books retrieve successfully"),
            @ApiResponse(responseCode = "204", description = "Books not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBookById(@PathVariable Long id) {
        try {
            log.info("Fetching Book by Id" + id);

            Optional<BookResponseDTO> book = bookService.findById(id);
            if (book.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(book);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return null;
    }


}
