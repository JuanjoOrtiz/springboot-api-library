package com.springboot.api.library.dtos;

import com.springboot.api.library.entities.MemberStatus;
import com.springboot.api.library.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponseDTO(
        Long id,
        String identificationNumber,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        LocalDateTime createAt,
        MemberStatus status,
        int  maxBooksAllowed,
        User user
) {
}
