package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.configs.JwtService;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.request.TokenRefreshRequest;
import com.desmond.gadgetstore.payload.response.ApiResponse;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.payload.response.RefreshTokenResponse;
import com.desmond.gadgetstore.payload.response.ResponseUtil;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.repositories.RefreshTokenRepository;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.AuthService;
import com.desmond.gadgetstore.services.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody CreateUser request) {
        UserResponse createdUser = authService.register(request);
        
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/v1/{userId}").buildAndExpand(createdUser.getId()).toUri();
        
        return ResponseEntity
        		.created(location)
        		.body(ResponseUtil.success("User registered successfully", createdUser, null));
    }

    @PostMapping("validate-email")
    public ResponseEntity<ApiResponse<UserResponse>> authenticateUser(
    		@Valid @RequestBody AuthenticateEmailRequest authRequest
    	) {
        UserResponse authenticatedUser = authService.validateEmail(authRequest);
        return ResponseEntity.ok(ResponseUtil.success("Email successfully validated", authenticatedUser, null));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        
        return ResponseEntity.ok(ResponseUtil.success("User login successfully", authResponse, null));
    }
    
    @PostMapping("/refreshtoken")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
      RefreshTokenResponse response = refreshTokenService.refreshToken(request);
      return ResponseEntity.ok(ResponseUtil.success("User login successfully", response, null));
    }
}
