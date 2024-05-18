package com.project.api.library.service.auth;

import com.project.api.library.dto.auth.AuthResponseDTO;
import com.project.api.library.dto.auth.LoginRequestDTO;
import com.project.api.library.dto.auth.RegisterRequestDTO;
import com.project.api.library.entity.auth.Role;
import com.project.api.library.entity.auth.User;
import com.project.api.library.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails user=userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponseDTO.builder()
                .token(token)
                .build();

    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode( registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .build();

    }
}
