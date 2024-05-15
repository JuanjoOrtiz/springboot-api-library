package com.project.api.library.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookDTO {

    private Long id;
    private String isbn;
    private String title;
    private AuthorDTO author;
    private String publicationDate;
    private GenderDTO gender;
    private PublisherDTO publisher;
    private ShelfDTO shelf;



}
