package com.nexus.controller;

import com.nexus.dto.AuthDto;
import com.nexus.model.User;
import com.nexus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "NEXUS API"));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuthDto.UserDto>> getAllUsers() {
        List<AuthDto.UserDto> users = userRepository.findAll()
            .stream()
            .map(AuthDto.UserDto::from)
            .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalUsers = userRepository.count();
        long adminCount = userRepository.findAll().stream()
            .filter(u -> u.getRole() == User.Role.ADMIN).count();
        return ResponseEntity.ok(Map.of(
            "totalUsers", totalUsers,
            "adminCount", adminCount
        ));
    }
}
