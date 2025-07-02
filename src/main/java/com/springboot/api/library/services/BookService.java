package com.springboot.api.library.services;

import com.springboot.api.library.dtos.BookRequestDTO;
import com.springboot.api.library.dtos.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

    Page<BookResponseDTO> findAll(Pageable pageable);
    Optional<BookResponseDTO> findById(Long id);
    BookResponseDTO create(BookRequestDTO bookRequestDTO);
    BookResponseDTO update(BookRequestDTO bookRequestDTO, Long id);
    void delete(Long id);

}
