package com.project.api.library.controller;

import com.project.api.library.entity.Book;
import com.project.api.library.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {


    private final BookService bookService;


    @GetMapping("/books")
    public ResponseEntity<List<Book>> findAllBooks(){
        List<Book> books = bookService.findAll();


            return ResponseEntity.ok().body(books);

    }

  /*  @GetMapping("/book/{id}")
    public Optional<Book> findById(@PathVariable Long id){
        return bookService.findById(id);
    }*/

    /*@PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book) {

        return ResponseEntity.status(HttpStatus.CREATED).body(book.save(book));

    }*/

}
