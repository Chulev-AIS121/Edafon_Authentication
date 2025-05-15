package com.example.Edafon_Authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для входа пользователя")
public class LoginRequest {

        @Schema(description = "Имя пользователя", example = "john_doe", required = true)
        @NotBlank(message = "Username is required")
        private String username;

        @Schema(description = "Пароль", example = "MySecret123!", required = true)
        @NotBlank(message = "Password is required")
        private String password;

        @Schema(description = "Роль пользователя", example = "USER", required = true)
        @NotBlank(message = "Role is required")
        private String role;

        public LoginRequest(String username, String password) {
                this.username = username;
                this.password = password;
        }

        // Геттеры и сеттеры
        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

}
