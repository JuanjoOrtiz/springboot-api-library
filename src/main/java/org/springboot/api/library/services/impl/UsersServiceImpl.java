package org.springboot.api.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springboot.api.library.dtos.UserDTO;
import org.springboot.api.library.exceptions.ServiceException;
import org.springboot.api.library.mapper.UserMapper;
import org.springboot.api.library.repositories.UserRepository;
import org.springboot.api.library.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        try {
            log.info("Retrieving all users with pageable: {}", pageable);
            return userRepository.findAll(pageable).map(UserMapper.INSTANCE::toDTO);
        } catch (Exception e) {
            log.error("Unexpected error retrieving all users", e);
            throw new ServiceException("Error retrieving all users", e);
        }
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getByUserNumber(String userNumber) {
        log.info("Searching userNumber by id: {}", userNumber);
        try {
            return userRepository.findByUserNumberContaining(userNumber)
                    .map(UserMapper.INSTANCE::toDTO);
        } catch (Exception e) {
            log.error("Error retrieving user with ID: {}", userNumber, e);
            throw new ServiceException(STR."Error retrieving user with ID: \{userNumber}", e);
        }
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
