package org.springboot.api.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 50 characters")
    private String title;
    @NotBlank(message = "Author cannot be blank")
    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String author;
    @NotBlank(message = "ISBN cannot be blank")
    @Pattern(regexp = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|[0-9X]{10}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "Invalid ISBN format")
    private String isbn;
    @NotBlank(message = "Publisher cannot be blank")
    @Size(min = 1, max = 100, message = "Publisher name must be between 1 and 100 characters")
    private String  publisher;
    @NotNull(message = "Publication year cannot be null")
    @PastOrPresent(message = "Publication year cannot be in the future")
    private LocalDate publication_year;
    @NotBlank(message = "Category cannot be blank")
    @Size(min = 1, max = 50, message = "Category must be between 1 and 50 characters")
    private String category;
    @NotNull(message = "Available quantity cannot be null")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private int available_quantity;
    @Size(max = 255, message = "Route image path must not exceed 255 characters")
    private String route_image;

}
