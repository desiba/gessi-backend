package com.desmond.gadgetstore.services;

import java.util.UUID;

import com.desmond.gadgetstore.entities.RefreshTokenEntity;
import com.desmond.gadgetstore.payload.request.TokenRefreshRequest;
import com.desmond.gadgetstore.payload.response.RefreshTokenResponse;

public interface RefreshTokenService {
	RefreshTokenEntity createRefreshToken(UUID userId);
	RefreshTokenResponse refreshToken(TokenRefreshRequest request);
	RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
	int deleteByUserId(UUID userId);
}
