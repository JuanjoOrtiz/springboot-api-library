package com.project.api.library.dto.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegisterUserDTO {

    private String email;

    private String password;

    private String fullName;

}
