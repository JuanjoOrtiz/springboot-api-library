package com.project.api.library.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;
    private String isbn;
    private String title;
    private AuthorDTO author;
    private LocalDateTime publicationDate;
    private GenderDTO gender;
    private PublisherDTO publisher;
    private ShelfDTO shelf;

    public BookDTO(String title) {
        this.title = title;
    }


}
