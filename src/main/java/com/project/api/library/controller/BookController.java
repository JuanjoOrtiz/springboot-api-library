package com.project.api.library.controller;

import com.project.api.library.dto.BookDTO;
import com.project.api.library.entity.Book;
import com.project.api.library.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {


    private final BookService bookService;


    @GetMapping("/books")
    public ResponseEntity<Page<BookDTO>> findAllBooks( ){
        Pageable pageable = PageRequest.of(0, 5, Sort.by("title").ascending());
        Page<BookDTO> books = bookService.findAll(pageable);
        return ResponseEntity.ok().body(books);

    }

    @GetMapping("/book/{id}")
    public Optional<BookDTO> findById(@PathVariable Long id){
        return bookService.findById(id);
    }

    @PostMapping("/book")
    public ResponseEntity<BookDTO> save(@RequestBody BookDTO bookDTO) {
        BookDTO savedBookDTO = bookService.save(bookDTO);
        return ResponseEntity.ok().body(savedBookDTO);

    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO bookDTO){
        bookDTO = bookService.update(id, bookDTO);
        return ResponseEntity.ok().body(bookDTO);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }

}
