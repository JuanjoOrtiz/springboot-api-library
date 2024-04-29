package com.project.api.library.dto;


import lombok.Data;

@Data
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String publicationDate;
    private String gender;
    private String editorial;
    private String shelf;
}
