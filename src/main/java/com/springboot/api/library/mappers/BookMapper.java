package com.springboot.api.library.mappers;

import com.springboot.api.library.dtos.BookRequestDTO;
import com.springboot.api.library.dtos.BookResponseDTO;
import com.springboot.api.library.entities.Book;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper extends GenericMapper<Book, BookRequestDTO, BookResponseDTO> {

    @Mapping(source = "publicationYear", target = "publicationYear", dateFormat = "dd/MM/yyyy")
    public BookResponseDTO toDTO(Book entity);

    @Mapping(target = "id", ignore = true)
    public Book toEntity(BookRequestDTO requestDTO);


    public void updateEntityFromDto(BookRequestDTO requestDTO,@MappingTarget Book entity);
}
