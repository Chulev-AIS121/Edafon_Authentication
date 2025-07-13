package com.example.Edafon_Authentication.service;

import com.example.Edafon_Authentication.dto.LoginRequest;
import com.example.Edafon_Authentication.dto.RegisterRequest;
import com.example.Edafon_Authentication.entity.User;
import com.example.Edafon_Authentication.exceptions.AccountDisabledException;
import com.example.Edafon_Authentication.exceptions.EmailAlreadyExistsException;
import com.example.Edafon_Authentication.exceptions.InvalidCredentialsException;
import com.example.Edafon_Authentication.exceptions.UsernameAlreadyExistsException;
import com.example.Edafon_Authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public void register(RegisterRequest request) {
        log.info("Attempting to register user: {}", request.getUsername());
        // Проверка username и  email
        try {
            log.debug("Checking username uniqueness for: {}", request.getUsername());
            if (userRepository.existsByUsername(request.getUsername())) {
                log.warn("Username already exists: {}", request.getUsername());
                throw new UsernameAlreadyExistsException("Username already exists");
            }
            log.debug("Checking email uniqueness for: {}", request.getEmail());
            if (userRepository.existsByEmail(request.getEmail())) {
                log.warn("Email already exists: {}", request.getEmail());
                throw new EmailAlreadyExistsException("Email already exists");
            }

            // Создание пользователя
            log.debug("Creating new user entity");
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setActive(true);

            // Сохранение
            log.info("Saving new user: {}", user.getUsername());
            User savedUser = userRepository.save(user);
            log.info("User registered successfully with ID: {}", savedUser.getId());

        } catch (Exception e) {
            log.error("Registration failed for user: {}", request.getUsername(), e);
            throw e;
        }
    }
    public String authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        if (!user.isActive()) {
            throw new AccountDisabledException("User account is inactive");
        }

        return jwtTokenUtil.generateToken(user.getUsername(), user.getRole());
    }

}
