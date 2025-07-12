package com.desmond.gadgetstore.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import com.desmond.gadgetstore.entities.TokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Map;

public interface JwtTokenProvider {
	TokenEntity generateAccessToken(Map<String, Object> extraClaims, long duration, TemporalUnit durationType, UserEntity user);
	TokenEntity generateRefreshToken(long duration,  TemporalUnit durationType, UserEntity user);
    boolean validateToken(String tokenValue);
    String getUsernameFromToken(String tokenValue);
    LocalDateTime getExpiryDateFromToken(String tokenValue);
}
