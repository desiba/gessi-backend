package com.desmond.gadgetstore.services.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.desmond.gadgetstore.configs.JwtService;
import com.desmond.gadgetstore.entities.RefreshTokenEntity;
import com.desmond.gadgetstore.exceptions.TokenRefreshException;
import com.desmond.gadgetstore.payload.request.TokenRefreshRequest;
import com.desmond.gadgetstore.payload.response.RefreshTokenResponse;
import com.desmond.gadgetstore.repositories.RefreshTokenRepository;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.RefreshTokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
	@Value("${server.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

    private final JwtService jwtService;

	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	public Optional<RefreshTokenEntity> findByToken(String token) {
	   return refreshTokenRepository.findByToken(token);
	}

	public RefreshTokenEntity createRefreshToken(UUID userId) {
	  RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
			  .user(userRepository.findById(userId).get())
			  .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
			  .token(UUID.randomUUID().toString())
			  .build();

	  refreshToken = refreshTokenRepository.save(refreshToken);
	  return refreshToken;
	}
	  
	public RefreshTokenResponse refreshToken(TokenRefreshRequest request) {
		  String requestRefreshToken = request.getRefreshToken();
		  return this.findByToken(requestRefreshToken)
			        .map(this::verifyExpiration)
			        .map(RefreshTokenEntity::getUser)
			        .map(user -> {
			        	
			          String token = jwtService.generateTokenFromEmail(user.getEmail());
			          return RefreshTokenResponse.builder()
			        		  .accessToken(token)
			        		  .refreshToken(requestRefreshToken)
			        		  .build();
			         
			        })
			        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
			            "Refresh token is not in database!"));
	}

	public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
	    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
	      refreshTokenRepository.delete(token);
	      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
	    }

	    return token;
	}

	@Transactional
	public int deleteByUserId(UUID userId) {
	    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}
