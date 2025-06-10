package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.common.utils.Helper;
import com.desmond.gadgetstore.common.utils.UserRole;
import com.desmond.gadgetstore.configs.JwtService;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ConstraintViolationException;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public UserEntity register(CreateUser request) {
        String token = Helper.generateRandom(20);
        Optional<UserEntity> registeredUser = userRepository.findByEmail(request.getEmail());
        
        if(registeredUser.isEmpty()) {
        	
       	 var user = UserEntity.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        }
        
       return userRepository.findByEmail(request.getEmail()).get();
    }

    public UserEntity validateEmail(AuthenticateEmailRequest authRequest) {

        var user = userRepository.findByEmailToken(authRequest.getToken()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_USER_NOT_FOUND));

        if (!user.isActive()) {
             user.setEmailValid(true);
        }
       
        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
    	
    	Optional<UserEntity> user = userRepository.findByEmail(request.getEmail());
    	
    	if(user.isEmpty()) {
    		throw new ResourceNotFoundException(ErrorMessages.ERROR_USER_NOT_FOUND);
    	}
    	
    	
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
         
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder().accessToken(jwtToken).user(user.get()).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
