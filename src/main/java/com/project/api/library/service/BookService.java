package com.project.api.library.service;

import com.project.api.library.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface BookService {
    Page<BookDTO> findAll(Pageable pageable);

    Optional<BookDTO> findById(Long id);

    BookDTO save(BookDTO bookDTO);

    BookDTO update(Long id, BookDTO bookDTO);

    void delete(Long id);
}
