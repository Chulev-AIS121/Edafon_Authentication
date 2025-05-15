package com.example.Edafon_Authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ, содержащий JWT токен")
public class TokenResponse {

    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }
    // Геттеры и сеттеры
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
