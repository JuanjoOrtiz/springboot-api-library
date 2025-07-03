package com.springboot.api.library.dtos;

import com.springboot.api.library.entities.Status;
import com.springboot.api.library.entities.User;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberRequestDTO(
        String identificationNumber,
        String firstName,
        String lastName,
        @Email
        String email,
        String phone,
        LocalDate dateOfBirth,
        LocalDateTime createAt,
        Status status,
        int  maxBooksAllowed,
        User user
) {
}
