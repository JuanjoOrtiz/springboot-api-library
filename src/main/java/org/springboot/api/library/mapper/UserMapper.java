package org.springboot.api.library.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springboot.api.library.dtos.UserDTO;
import org.springboot.api.library.entities.User;

@Mapper
public interface UserMapper extends GenericMapper<User, UserDTO> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
