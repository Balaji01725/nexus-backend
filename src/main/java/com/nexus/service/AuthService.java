package com.nexus.service;

import com.nexus.dto.AuthDto;
import com.nexus.model.User;
import com.nexus.repository.UserRepository;
import com.nexus.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthDto.AuthResponse login(AuthDto.LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("No account found with this email"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthDto.AuthResponse.builder()
            .token(token).user(AuthDto.UserDto.from(user)).build();
    }

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("An account with this email already exists");
        }
        User user = User.builder()
            .name(req.getName()).email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .role(User.Role.USER).build();
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthDto.AuthResponse.builder()
            .token(token).user(AuthDto.UserDto.from(user)).build();
    }
}
