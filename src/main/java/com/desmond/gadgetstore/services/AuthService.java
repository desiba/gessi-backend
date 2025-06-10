package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService extends UserDetails {
    UserEntity register(CreateUser createUser);

    AuthResponse login(LoginRequest loginRequest);

    UserEntity validateEmail(AuthenticateEmailRequest request);
}
