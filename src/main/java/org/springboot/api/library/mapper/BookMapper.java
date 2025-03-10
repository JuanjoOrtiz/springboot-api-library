package org.springboot.api.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.entities.Book;

@Mapper
public interface BookMapper extends GenericMapper<Book, BookDTO> {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
}
