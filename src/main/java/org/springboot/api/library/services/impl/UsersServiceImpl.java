package org.springboot.api.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springboot.api.library.dtos.UserDTO;
import org.springboot.api.library.entities.User;
import org.springboot.api.library.entities.UserRole;
import org.springboot.api.library.exceptions.NotFoundException;
import org.springboot.api.library.exceptions.ServiceException;
import org.springboot.api.library.mapper.UserMapper;
import org.springboot.api.library.repositories.UserRepository;
import org.springboot.api.library.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

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
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("user ID must be a positive number");
        }
        log.info("Searching user by id: {}", id);
        try {
            return userRepository.findById(id)
                    .map(UserMapper.INSTANCE::toDTO);
        } catch (Exception e) {
            log.error("Error retrieving user with ID: {}", id, e);
            throw new ServiceException(STR."Error retrieving user with ID: \{id}", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getByUserNumber(int userNumber) {
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
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
   //     validateUserDTO(userDTO);
        try {
            log.info("Creating new user: {}", userDTO);

            // 1. Obtener el próximo número de usuario (secuencial)
            int nextUserNumber = (int) (userRepository.findMaxUserNumber()+1L);

            return Optional.of(userDTO)
                    .map(dto -> {

                        User user = UserMapper.INSTANCE.toEntity(dto);

                        // 3. Asignar valores automáticos
                        user.setUserNumber(nextUserNumber);
                        user.setCreatedAt(LocalDateTime.now());

                        // 4. Asignar rol por defecto si no hay roles
                        if (user.getRole().isEmpty()) {
                            user.getRole().add(UserRole.USER);
                        }

                        return user;
                    })
                    .map(userRepository::save) // 5. Guardar en la base de datos
                    .map(UserMapper.INSTANCE::toDTO) // 6. Convertir a DTO
                    .orElseThrow(() -> new ServiceException("Error in user creation"));
        } catch (Exception e) {
            log.error("Error creating user: {}", userDTO, e);
            throw new ServiceException("Error creating user", e);
        }

    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Updating user with ID: {} with data: {}", id, userDTO);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        validateUserDTO(userDTO);

        Consumer<User> updateFields = user -> {

            user.setFirstName(userDTO.firstName());
            user.setLastName(userDTO.lastName());
            user.setEmail(userDTO.email());
            user.setPassword(userDTO.password());
        };

        return userRepository.findById(id)
                .map(user -> {
                    try {
                        updateFields.accept(user);
                        return userRepository.save(user);
                    } catch (Exception e) {
                        log.error("Error updating user entity with ID: {}", id, e);
                        throw new ServiceException(STR."Error updating user with ID: \{id}", e);
                    }
                })
                .map(UserMapper.INSTANCE::toDTO)
                .orElseThrow(() -> new NotFoundException(STR."User not found with ID: \{id}"));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        try {
            userRepository.findById(id)
                    .ifPresentOrElse(
                            user -> userRepository.deleteById(id),
                            () -> { throw new NotFoundException(STR."User not found with ID: \{id}"); }
                    );
        } catch (Exception e) {
            log.info("Successfully deleted user with ID: {}", id);
            throw new ServiceException(STR."Error deleting user with ID: \{id}", e);
        }
    }

    private void validateUserDTO(UserDTO userDTO) {
        log.debug("Validating UserDTO: {}", userDTO);
        Optional.ofNullable(userDTO)
                .orElseThrow(() -> {
                    log.warn("Null UserDTO provided");
                    return new IllegalArgumentException("UserDTO cannot be null");
                });

        Optional.ofNullable(userDTO.firstName())
                .filter(firstName -> !firstName.trim().isEmpty())
                .orElseThrow(() -> {
                    log.warn("Invalid firstName in UserDTO: {}", userDTO);
                    return new IllegalArgumentException("firstName cannot be null or empty");
                });

        Optional.ofNullable(userDTO.lastName())
                .filter(lastName -> !lastName.trim().isEmpty())
                .orElseThrow(() -> {
                    log.warn("Invalid lastName in UserDTO: {}", userDTO);
                    return new IllegalArgumentException("lastName cannot be null or empty");
                });

        Optional.ofNullable(userDTO.email())
                .filter(email -> !email.trim().isEmpty())
                .orElseThrow(() -> {
                    log.warn("Invalid email in UserDTO: {}", userDTO);
                    return new IllegalArgumentException("email cannot be null or empty");
                });
        Optional.ofNullable(userDTO.password())
                .filter(password -> !password.trim().isEmpty())
                .orElseThrow(() -> {
                    log.warn("Invalid password in UserDTO: {}", userDTO);
                    return new IllegalArgumentException("password cannot be null or empty");
                });

        log.debug("UserDTO validation successful");
    }
}
