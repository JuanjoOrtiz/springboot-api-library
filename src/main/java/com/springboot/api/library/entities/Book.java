package com.springboot.api.library.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String publisher;
    private LocalDate publicationYear;
    @Column(unique = true, nullable = false)
    private String isbn;
    @Column(nullable = false, precision = 0)
    private Integer totalQuantity;
    @Column(nullable = false, precision = 0)
    private Integer availableQuantity;



}
