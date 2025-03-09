package org.springboot.api.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.entities.Book;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    // Convierte de Book a BookDTO
    BookDTO toDTO(Book book);

    // Convierte de BookDTO a Book
    Book toEntity(BookDTO bookDTO);
}
