package com.springboot.api.library.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookResponseDTO(
        Long id,
        @NotEmpty
        String title,
        String author,
        String publisher,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate publicationYear,
        @NotEmpty
        String isbn,
        @Positive
        Integer totalQuantity,
        @Positive
        Integer availableQuantity

) {
}
