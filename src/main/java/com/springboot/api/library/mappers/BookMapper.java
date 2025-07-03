package com.springboot.api.library.mappers;

import com.springboot.api.library.dtos.BookResponseDTO;
import com.springboot.api.library.entities.Book;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper extends GenericMapper<Book, BookResponseDTO> {

    @Mapping(source = "publicationYear", target = "publicationYear", dateFormat = "dd/MM/yyyy")
    public BookResponseDTO toDTO(Book entity);

    @Mapping(target = "id", ignore = true)
    public Book toEntity(BookResponseDTO dto);


    public void updateEntityFromDto(BookResponseDTO dto, Book entity);
}
