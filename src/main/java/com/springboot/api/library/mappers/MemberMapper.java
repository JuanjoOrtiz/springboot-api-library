package com.springboot.api.library.mappers;

import com.springboot.api.library.dtos.MemberRequestDTO;
import com.springboot.api.library.dtos.MemberResponseDTO;
import com.springboot.api.library.entities.Member;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MemberMapper {

    MemberResponseDTO toDTO(Member entity);

    Member toEntity(MemberRequestDTO requestDto);

    void updateEntityFromDto(MemberRequestDTO requestDto, @MappingTarget Member entity);

}
