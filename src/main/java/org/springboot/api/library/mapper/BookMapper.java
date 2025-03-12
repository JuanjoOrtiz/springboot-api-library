package org.springboot.api.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.entities.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    // Convierte de Book a BookDTO
    @Mappings({

            @Mapping(source = "title", target = "title"),
            @Mapping(source = "author", target = "author"),
            @Mapping(source = "isbn", target = "isbn"),
            @Mapping(source = "publisher", target = "publisher"),
            @Mapping(source = "publicationDate", target = "publicationDate"),
            @Mapping(source = "category", target = "category"),
            @Mapping(source = "availableQuantity", target = "availableQuantity"),
            @Mapping(source = "routeImage", target = "routeImage")
    })
    BookDTO toDTO(Book book);

    // Convierte de BookDTO a Book
    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "author", target = "author"),
            @Mapping(source = "isbn", target = "isbn"),
            @Mapping(source = "publisher", target = "publisher"),
            @Mapping(source = "publicationDate", target = "publicationDate"),
            @Mapping(source = "category", target = "category"),
            @Mapping(source = "availableQuantity", target = "availableQuantity"),
            @Mapping(source = "routeImage", target = "routeImage")
    })
    Book toEntity(BookDTO bookDTO);

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Named("mapLocalDateToString")
    default String mapLocalDateToString(LocalDate date) {
        return (date != null) ? date.format(FORMATTER) : null;
    }
}
