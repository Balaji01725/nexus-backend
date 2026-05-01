package com.nexus.dto;

import com.nexus.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @Email(message = "Valid email required")
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 4, message = "Password min 4 chars")
        private String password;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @NotBlank
        private String name;

        @Email(message = "Valid email required")
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 6, message = "Password min 6 chars")
        private String password;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private UserDto user;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UserDto {
        private String id;
        private String name;
        private String email;
        private String role;

        public static UserDto from(User user) {
            return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
        }
    }
}
