package org.springboot.api.library.dtos;

import lombok.Builder;


import java.time.LocalDate;

import java.time.LocalDate;

public record BookDTO(
                      String title,
                      String author,
                      String isbn,
                      String publisher,
                      LocalDate publicationDate,
                      String category,
                      int availableQuantity,
                      String routeImage) {


}
