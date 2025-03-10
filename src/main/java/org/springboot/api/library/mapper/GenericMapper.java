package org.springboot.api.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springboot.api.library.dtos.BookDTO;
import org.springboot.api.library.entities.Book;

@Mapper
public interface GenericMapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);

    // Método para obtener la instancia del mapper
    default GenericMapper<E, D> getInstance() {
        return Mappers.getMapper(this.getClass());
    }
}

