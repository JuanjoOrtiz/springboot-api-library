package com.springboot.api.library.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper<E,RQ,RS> {

    RS toDTO(E entity);
    E toEntity(RQ requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy  = NullValuePropertyMappingStrategy.IGNORE )
    void updateEntityFromDto(RQ requestDto, @MappingTarget E entity);

}
