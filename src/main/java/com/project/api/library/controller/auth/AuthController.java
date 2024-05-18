package com.project.api.library.controller.auth;

import com.project.api.library.dto.auth.AuthResponseDTO;
import com.project.api.library.dto.auth.LoginRequestDTO;
import com.project.api.library.dto.auth.RegisterRequestDTO;
import com.project.api.library.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

}
