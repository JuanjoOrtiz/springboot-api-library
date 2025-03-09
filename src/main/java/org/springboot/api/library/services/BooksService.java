package org.springboot.api.library.services;



import org.springboot.api.library.dtos.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface BooksService {
    Page<BookDTO> getAllBooks(Pageable pageable);
    Optional<BookDTO> getBookById(Long id);
    BookDTO createBook(BookDTO bookDTO);
    BookDTO updateBook(Long id, BookDTO updatedBookDTO);
    void deleteBook(Long id);
}
