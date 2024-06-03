package com.project.api.library.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    @NotNull
    @NotBlank(message = "Isbn is required")
    private String isbn;
    @NotNull
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull
    private AuthorDTO author;
    @NotNull
    private LocalDateTime publicationDate;
    @NotNull
    private GenderDTO gender;
    @NotNull
    private PublisherDTO publisher;
    @NotNull

    private ShelfDTO shelf;

    public BookDTO(String title) {
        this.title = title;
    }


}
