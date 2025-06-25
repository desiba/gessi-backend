package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.common.utils.Helper;
import com.desmond.gadgetstore.common.utils.UserRole;
import com.desmond.gadgetstore.configs.JwtService;
import com.desmond.gadgetstore.entities.RefreshTokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ConstraintViolationException;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.payload.response.RefreshTokenResponse;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.AuthService;
import com.desmond.gadgetstore.services.RefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private static final long serialVersionUID = 1L;
	
	private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final ModelMapper modelMapper;

    public UserResponse register(CreateUser request) {
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
        
       UserEntity createdUser = userRepository.findByEmail(request.getEmail()).get();
       
       return modelMapper.map(createdUser, UserResponse.class);
    }

    public UserResponse validateEmail(AuthenticateEmailRequest authRequest) {

        var user = userRepository.findByEmailToken(authRequest.getToken()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_USER_NOT_FOUND));

        if (!user.isActive()) {
             user.setEmailValid(true);
        }
        
        UserEntity updatedUser = userRepository.save(user);
       
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    public AuthResponse login(LoginRequest request) {
    	
    	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    	
    	SecurityContextHolder.getContext().setAuthentication(authentication);	
    	
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(userDetails);
        
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponse.builder()
        		.token(jwtService.generateToken(userDetails))
        		.refreshToken(refreshToken.getToken())
        		.user(refreshToken.getUser())
        		.build();
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
