package com.example.Edafon_Authentication.controller;

import com.example.Edafon_Authentication.dto.LoginRequest;
import com.example.Edafon_Authentication.dto.RegisterRequest;
import com.example.Edafon_Authentication.dto.TokenResponse;
import com.example.Edafon_Authentication.service.AuthService;
import com.example.Edafon_Authentication.service.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Методы регистрации, входа и валидации JWT токена")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя",
               description = "Создаёт нового пользователя с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "409", description = "Пользователь с такими данными уже существует")
    })
    public ResponseEntity<?> register(
            @Parameter(description = "Данные для регистрации", required = true)
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя", description = "Аутентификация по имени пользователя и паролю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная авторизация и получение JWT токена"),
            @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль")
    })
    public ResponseEntity<?> login(
            @Parameter(description = "Данные для входа", required = true)
            @Valid @RequestBody LoginRequest request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @GetMapping("/validate-token")
    @Operation(summary = "Проверка JWT токена", description = "Проверяет валидность и срок действия токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен валиден"),
            @ApiResponse(responseCode = "401", description = "Токен просрочен или недействителен")
    })
    public ResponseEntity<?> validateToken(
            @Parameter(description = "JWT токен для проверки", required = true)
            @RequestParam("token") String token) {
        if (jwtTokenUtil.isTokenExpired(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
        }

        String username = jwtTokenUtil.extractUsername(token);
        if (username == null || !jwtTokenUtil.validateToken(token, username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return ResponseEntity.ok("Token is valid");
    }
}
