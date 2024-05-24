package com.project.api.library.entity.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponse {
    private String token;
    private long expiresIn;
}
