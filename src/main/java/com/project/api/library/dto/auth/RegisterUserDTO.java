package com.project.api.library.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    private String email;

    private String password;

    private String fullName;

    public RegisterUserDTO setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public RegisterUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterUserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

}
