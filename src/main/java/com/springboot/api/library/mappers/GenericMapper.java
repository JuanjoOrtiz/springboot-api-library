package com.springboot.api.library.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper<E,D> {

    D toDTO(E entity);
    E toEntity(D dto);

    @BeanMapping(nullValuePropertyMappingStrategy  = NullValuePropertyMappingStrategy.IGNORE )
    void updateEntityFromDto(D dto, @MappingTarget E entity);

}
