package com.bootcamp.onlineschool.service;

import com.bootcamp.onlineschool.dto.AuthResponse;
import com.bootcamp.onlineschool.dto.LoginRequest;
import com.bootcamp.onlineschool.dto.RegisterRequest;
import com.bootcamp.onlineschool.dto.UserInfoResponse;
import com.bootcamp.onlineschool.entity.AppUser;
import com.bootcamp.onlineschool.exception.ResourceNotFoundException;
import com.bootcamp.onlineschool.exception.ValidationException;
import com.bootcamp.onlineschool.repository.AppUserRepository;
import com.bootcamp.onlineschool.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered: " + request.getEmail());
        }

        AppUser user = new AppUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getRole()
        );
        AppUser saved = userRepository.save(user);

        String token = jwtService.generateToken(saved.getUsername(), saved.getRole().name());
        return new AuthResponse(token, jwtService.getExpirationMs(), saved.getUsername(),
                saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new ValidationException("Invalid username or password");
        }

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.getUsername()));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, jwtService.getExpirationMs(), user.getUsername(),
                user.getEmail(), user.getRole());
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getCurrentUser(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserInfoResponse.fromEntity(user);
    }
}
