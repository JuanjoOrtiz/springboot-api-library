package com.springboot.api.library.dtos;

import java.time.LocalDate;

public record BookDTO(
        Long id,
        String title,
        String author,
        String publisher,
        LocalDate publicationYear,
        String isbn,
        Integer totalQuantity,
        Integer availableQuantity

) {
}
