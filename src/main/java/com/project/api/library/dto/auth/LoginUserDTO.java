package com.project.api.library.dto.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginUserDTO {

    private String email;

    private String password;
}
