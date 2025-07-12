package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.common.utils.CookieUtil;
import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.common.utils.Helper;
import com.desmond.gadgetstore.common.utils.UserRole;
import com.desmond.gadgetstore.entities.TokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.jwt.JwtTokenProvider;
import com.desmond.gadgetstore.payload.request.AuthenticateEmailRequest;
import com.desmond.gadgetstore.payload.request.CreateUser;
import com.desmond.gadgetstore.payload.request.LoginRequest;
import com.desmond.gadgetstore.payload.response.AuthResponse;
import com.desmond.gadgetstore.payload.response.LoginResponse;
import com.desmond.gadgetstore.payload.response.RefreshTokenResponse;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.repositories.TokenRepository;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private static final long serialVersionUID = 1L;
	
	@Value("${jwt.access.duration.minute}")
    private long accessTokenDurationMinute;
	
    @Value("${jwt.access.duration.second}")
    private long accessTokenDurationSecond;
    
    @Value("${jwt.refresh.duration.day}")
    private long refreshTokenDurationDay;
    
    @Value("${jwt.refresh.duration.second}")
    private long refreshTokenDurationSecond;
	
	private final UserRepository userRepository;
	
	private final TokenRepository tokenRepository;
  
    private final AuthenticationManager authenticationManager;
 
    private final ModelMapper modelMapper;
    private final JwtTokenProvider tokenProvider;
    private final CookieUtil cookieUtil;
    private final UserDetailsService userDetailsService;


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

        if (!user.isActive()) user.setEmailValid(true);
        
        UserEntity updatedUser = userRepository.save(user);
       
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request, String accessToken, String refreshToken) {
    	 
    	 Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                		 request.getEmail(), request.getPassword()
                 )
         );
    	 
    	 SecurityContextHolder.getContext().setAuthentication(authentication);	
     	
         UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
             	 
    	 Optional<UserEntity> fetchedUser = userRepository.findByEmail(userDetails.getEmail());
    	 
    	    	 
    	 UserEntity user = fetchedUser.get();
         //.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));    	 
    	 
    	 boolean accessTokenValid = tokenProvider.validateToken(accessToken);
         boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
         
         HttpHeaders responseHeaders = new HttpHeaders();
         TokenEntity newAccessToken, newRefreshToken;
         
         revokeAllTokenOfUser(user);
         
         if(!accessTokenValid && !refreshTokenValid) {
        	
        	 newAccessToken = tokenProvider.generateAccessToken(
                     Map.of("role", userDetails.getAuthorities()),
                     accessTokenDurationMinute,
                     ChronoUnit.MINUTES,
                     user
             );
        	 
        	 newRefreshToken = tokenProvider.generateRefreshToken(
                     refreshTokenDurationDay,
                     ChronoUnit.DAYS,
                     user
             );
        	 
        	 newAccessToken.setUser(user);
        	 
             newRefreshToken.setUser(user);
             
             tokenRepository.saveAll(List.of(newAccessToken, newRefreshToken));
             
             addAccessTokenCookie(responseHeaders, newAccessToken);
             
             addRefreshTokenCookie(responseHeaders, newRefreshToken);
         }
         
         if(!accessTokenValid && refreshTokenValid) {
        	
             newAccessToken = tokenProvider.generateAccessToken(
                 Map.of("role", userDetails.getAuthorities()),
                 accessTokenDurationMinute,
                 ChronoUnit.MINUTES,
                 user
             );
             addAccessTokenCookie(responseHeaders, newAccessToken);
         }
         
         if(accessTokenValid && refreshTokenValid) {

             newAccessToken = tokenProvider.generateAccessToken(
                 Map.of("role", userDetails.getAuthorities()),
                 accessTokenDurationMinute,
                 ChronoUnit.MINUTES,
                 user
             );

             newRefreshToken = tokenProvider.generateRefreshToken(
                 refreshTokenDurationDay,
                 ChronoUnit.DAYS,
                 user
             );

             newAccessToken.setUser(user);
             newRefreshToken.setUser(user);

             // save tokens in db
             tokenRepository.saveAll(List.of(newAccessToken, newRefreshToken));

             addAccessTokenCookie(responseHeaders, newAccessToken);
             addRefreshTokenCookie(responseHeaders, newRefreshToken);
         }
                  
         SecurityContextHolder.getContext().setAuthentication(authentication);
                  
         LoginResponse loginResponse = new LoginResponse(true, user.getRoles());

         return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }
    

	@Override
	public UserEntity authenticate(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    	
    	SecurityContextHolder.getContext().setAuthentication(authentication);	
    	
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
		return userRepository.findById(userDetails.getId()).get();
	}
	
	private void addAccessTokenCookie(HttpHeaders httpHeaders, TokenEntity token) {
	    httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token.getToken(), accessTokenDurationSecond).toString());
	}
	
	private void addRefreshTokenCookie(HttpHeaders httpHeaders, TokenEntity token) {
	    httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token.getToken(), refreshTokenDurationSecond).toString());
	}
	
	private void revokeAllTokenOfUser(UserEntity user) {
        // get all user tokens
        List<TokenEntity> tokens = tokenRepository.findAllByUser(user);
        System.out.println("=======TIME======");
        System.out.println(tokens.size());
        System.out.println("=======TIME======");
    	tokens.forEach(token -> {
	         if(token.getExpiryDate().isBefore(LocalDateTime.now())) {
	             tokenRepository.deleteById(token.getId());
	         	 System.out.println("=======DELETE======");
	         } else if(!token.isDisabled()) {
	             token.setDisabled(true);
	             tokenRepository.save(token);
	         }
        });
	}

	@Override
	public ResponseEntity<LoginResponse> refresh(String refreshToken) {
		 boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
		 
		 if(!refreshTokenValid)
	            new BadRequestException("Refresh token is invalid");
		 
		 String username = tokenProvider.getUsernameFromToken(refreshToken);
		 
		 UserEntity user = userRepository.findByEmail(username).orElseThrow(
                 () -> new ResourceNotFoundException("User not found")
         );
		 
		 UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		 
		 TokenEntity newAccessToken = tokenProvider.generateAccessToken(
	                Map.of("role", userDetails.getAuthorities()),
	                accessTokenDurationMinute,
	                ChronoUnit.MINUTES,
	                user
	        );
		 
		 HttpHeaders responseHeaders = new HttpHeaders();
		 addAccessTokenCookie(responseHeaders, newAccessToken);
		 
		 LoginResponse loginResponse = new LoginResponse(true, user.getRoles());

	     return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
	}

	@Override
	public ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
