package com.project.api.library.service.auth;

import com.project.api.library.dto.auth.AuthResponseDTO;
import com.project.api.library.dto.auth.LoginRequestDTO;
import com.project.api.library.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO loginRequest);
    AuthResponseDTO register(RegisterRequestDTO registerRequest);
}
