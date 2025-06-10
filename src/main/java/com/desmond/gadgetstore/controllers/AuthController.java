package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.ApiResponse;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.services.AuthService;
import com.desmond.gadgetstore.services.impl.AuthServiceImpl;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody CreateUser request) {
        UserEntity createdUser = authService.register(request);
        
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/{userId}").buildAndExpand(createdUser.getId()).toUri();
        
        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered successfully", createdUser, null));
    }

    @PostMapping("validate-email")
    public ResponseEntity<UserEntity> authenticateUser(@Valid @RequestBody AuthenticateEmailRequest authRequest) {
        UserEntity authenticatedUser = authService.validateEmail(authRequest);
        return new ResponseEntity<>(authenticatedUser, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

}
