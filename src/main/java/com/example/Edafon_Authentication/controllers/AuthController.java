package com.example.Edafon_Authentication.controllers;

import com.example.Edafon_Authentication.Service.LoginPerrsonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

@RestController
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final LoginPerrsonService loginService;

    public AuthController(LoginPerrsonService loginService) {
        this.loginService = loginService;
    }

    // Метод для маскировки username в логах
    private String maskUsername(String username) {
        if (username == null || username.isEmpty()) {
            return "[unknown]";
        }
        if (username.length() == 1) {
            return username.charAt(0) + "***";
        }
        return username.charAt(0) + "***" + username.charAt(username.length() - 1);
    }

    @GetMapping("/")
    public String loginGet() {
        logger.info("GET / - Отображение страницы входа");
        return "authentication/login.html";
    }

    @PostMapping("/")
    public ResponseEntity<String> loginPost(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        String maskedUsername = maskUsername(username);
        logger.info("Попытка входа: " + maskedUsername);

        loginService.setUsername(username);
        loginService.setPassword(password);

        boolean isLogin = loginService.login();

        if (isLogin) {
            logger.info("Успешный вход: " + maskedUsername);
            model.addAttribute("message", "Успешный вход!");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("X-Auth-Status", "success")
                    .body("redirect:/main");
        } else {
            logger.warning("Неудачная попытка входа: " + maskedUsername);
            model.addAttribute("message", "Введены неверные данные");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header("X-Auth-Status", "failed")
                    .body("authentication/login.html");
        }
    }
}