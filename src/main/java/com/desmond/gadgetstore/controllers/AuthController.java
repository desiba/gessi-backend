package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.ApiResponse;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.payload.response.LoginResponse;
import com.desmond.gadgetstore.payload.response.ResponseUtil;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<LoginResponse> login(
    		@CookieValue(name = "access_token", required = false) String accessToken,
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
    		@Valid @RequestBody LoginRequest loginRequest
    		) {
        return authService.login(loginRequest, accessToken, refreshToken);
    }
    
    @PostMapping("refresh")
    public ResponseEntity<LoginResponse> refreshtoken(
    		@CookieValue(name = "refresh_token", required = true) String refreshToken
    		) {
    	return authService.refresh(refreshToken);
    }
    
}
