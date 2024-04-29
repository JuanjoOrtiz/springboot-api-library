package com.project.api.library.service;

import com.project.api.library.dto.BookDTO;
import com.project.api.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
  /*  Optional<Book> findById(Long id);
    BookDTO save(Book book);
    BookDTO update(BookDTO theEmployeeDTO);
    void delete(int id);
*/
}
