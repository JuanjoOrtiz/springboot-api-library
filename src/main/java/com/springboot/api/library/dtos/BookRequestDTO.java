package com.springboot.api.library.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookRequestDTO(
       @NotEmpty
        String title,
       @NotEmpty
        String author,
       @NotEmpty
        String publisher,
       @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate publicationYear,
        @NotEmpty
        String isbn,
       @Positive
        Integer totalQuantity,
       @Positive
        Integer availableQuantity

) {
}
