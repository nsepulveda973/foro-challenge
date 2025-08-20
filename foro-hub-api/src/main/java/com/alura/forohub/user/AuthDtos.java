package com.alura.forohub.user;

import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public static class RegisterRequest {
        @NotBlank public String username;
        @NotBlank public String password;
    }
    public static class LoginRequest {
        @NotBlank public String username;
        @NotBlank public String password;
    }
    public static class AuthResponse {
        public String token;
        public AuthResponse(String token){ this.token = token; }
    }
}
