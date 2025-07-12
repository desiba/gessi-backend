package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.payload.response.LoginResponse;
import com.desmond.gadgetstore.payload.response.UserResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
	
    UserResponse register(CreateUser createUser);

    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String refreshToken);
    
    ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken);
        
    UserResponse validateEmail(AuthenticateEmailRequest request);
    
    UserEntity authenticate(LoginRequest request);    
    
}
