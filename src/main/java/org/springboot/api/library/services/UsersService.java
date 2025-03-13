package org.springboot.api.library.services;

import org.springboot.api.library.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsersService {
    Page<UserDTO> getAllUsers(Pageable pageable);
    Optional<UserDTO> getUserById(Long id);
    Optional<UserDTO> getByUserNumber(int userNumber);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO updatedUserDTO);
    void deleteUser(Long id);
}
