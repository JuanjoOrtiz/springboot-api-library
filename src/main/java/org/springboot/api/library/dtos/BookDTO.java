package org.springboot.api.library.dtos;

import lombok.Builder;


import java.time.LocalDate;

import java.time.LocalDate;

public record BookDTO(Long id,
                      String title,
                      String author,
                      String isbn,
                      String publisher,
                      LocalDate publicationYear,
                      String category,
                      int availableQuantity,
                      String routeImage) {


}
