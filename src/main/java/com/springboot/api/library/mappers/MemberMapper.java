package com.springboot.api.library.mappers;

import com.springboot.api.library.dtos.MemberRequestDTO;
import com.springboot.api.library.dtos.MemberResponseDTO;
import com.springboot.api.library.entities.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MemberMapper extends GenericMapper<Member, MemberRequestDTO, MemberResponseDTO> {

    @Override
    public MemberResponseDTO toDTO(Member entity);

    @Override
    public Member toEntity(MemberRequestDTO requestDto);

    @Override
    public void updateEntityFromDto(MemberRequestDTO requestDto, Member entity);
}
