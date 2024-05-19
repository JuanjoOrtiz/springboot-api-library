package com.project.api.library.controller;

import com.project.api.library.dto.BookDTO;
import com.project.api.library.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {


    private final BookService bookService;


    @GetMapping("/books")
    public ResponseEntity<Page<BookDTO>> findAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size){

        Page<BookDTO> books = bookService.findAll(PageRequest.of(page, size));
        return ResponseEntity.ok().body(books);

    }

    @GetMapping("/book/{id}")
    public Optional<BookDTO> findById(@PathVariable Long id){
        return bookService.findById(id);
    }

    @PostMapping("/book")
    public ResponseEntity<BookDTO> save(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO savedBookDTO = bookService.save(bookDTO);
        return ResponseEntity.ok().body(savedBookDTO);

    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id,@Valid  @RequestBody BookDTO bookDTO){
        bookDTO = bookService.update(id, bookDTO);
        return ResponseEntity.ok().body(bookDTO);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }

}
