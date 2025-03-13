package org.springboot.api.library.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springboot.api.library.dtos.UserDTO;
import org.springboot.api.library.exceptions.*;
import org.springboot.api.library.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

  @Autowired
  private UsersService usersService;


    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(@PageableDefault(size = 20, sort = "id") Pageable pageable) throws InvalidPaginationException {
        try {
            log.info("Fetching all users with pageable: {}", pageable);
            Page<UserDTO> users = usersService.getAllUsers(pageable);
            if (users.isEmpty()) {
                log.warn("No users found in the system");
                throw new NotFoundException("No users found in the system");
            }
            log.info("Successfully retrieved {} users", users.getTotalElements());
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            log.error("Invalid pagination parameters: {}", e.getMessage());
            throw new InvalidPaginationException(STR."Invalid pagination parameters: \{e.getMessage()}");
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error retrieving users: {}", e.getMessage());
            throw new RetrievalException(STR."Error retrieving users: \{e.getMessage()}");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") @Positive Long id) {
        try {
            log.info("Fetching user with id: {}", id);
            return usersService.getUserById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(STR."user not found with id: \{id}"));
        } catch (NotFoundException e) {
            log.debug("NotFoundException caught for user id: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error retrieving user with id: {}", id, e);
            throw new RetrievalException(STR."Error retrieving user with id \{id}: \{e.getMessage()}");
        }
    }

    @GetMapping(value = "/searchTerm", params = "searchTerm")
    public ResponseEntity<UserDTO> getuserByTitleOrISBN(@RequestParam("searchTerm") int searchTerm) {
        try {
            log.info("Starting search for user with title or ISBN: {}", searchTerm);
            return usersService.getByUserNumber(searchTerm)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(STR."user not found with title or ISBN: \{searchTerm}"));
        } catch (NotFoundException e) {
            log.debug("NotFoundException caught for search term: {}", searchTerm);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieving user with title or ISBN: {}", searchTerm, e);
            throw new RetrievalException(STR."Error retrieving title or ISBN \{searchTerm}: \{e.getMessage()}");
        }
    }


    @PostMapping
    public UserDTO createuser(@Valid @RequestBody UserDTO userDTO) {

        try {
            if (userDTO == null) {
                throw new InvalidDataException("user data cannot be null");
            }
            log.info("Creating new user: {}", userDTO);
            UserDTO createduser = usersService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createduser).getBody();
        } catch (InvalidDataException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate entry error while creating user: {}", e.getMessage());
            throw new CreationException(STR."Error creating user - duplicate entry: \{e.getMessage()}");
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            throw new CreationException(STR."Error creating user: \{e.getMessage()}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") @Positive Long id, @Valid @RequestBody UserDTO userDTO) {
        try {
            if (userDTO == null) {
                throw new InvalidDataException("user data cannot be null");
            }
            log.info("Updating user with id: {} with data: {}", id, userDTO);
            UserDTO updatedUserDTO = usersService.updateUser(id, userDTO);
            if (updatedUserDTO == null) {
                throw new NotFoundException(STR."user not found with id: \{id}");
            }
            return ResponseEntity.ok(updatedUserDTO);
        } catch (NotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating user with id {}: {}", id, e.getMessage());
            throw new UpdateException(STR."Error updating user with id \{id}: \{e.getMessage()}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteuser(@PathVariable("id") @Positive Long id) {
        try {
            log.info("Deleting user with id: {}", id);
            usersService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            log.error("user with id {} not found: ",e.getMessage());
            throw new DeletionException(STR."user with id \{id} not found: \{e.getMessage()}");
        } catch (Exception e) {
            log.error("Error deleting user with id {}: {}", id, e.getMessage());
            throw new DeletionException(STR."Error deleting user with id \{id}: \{e.getMessage()}");
        }
    }
}
