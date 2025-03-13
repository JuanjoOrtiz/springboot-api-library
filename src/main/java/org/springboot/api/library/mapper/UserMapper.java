package org.springboot.api.library.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import org.mapstruct.factory.Mappers;
import org.springboot.api.library.dtos.UserDTO;
import org.springboot.api.library.entities.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mappings({


            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
    })
    UserDTO toDTO(User User);


    @Mappings({

            @Mapping(source = "firstName", target = "firstName"),
            @Mapping(source = "lastName", target = "lastName"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
    })
    User toEntity(UserDTO userDTO);

}

