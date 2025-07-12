package com.desmond.gadgetstore.jwt;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.desmond.gadgetstore.entities.TokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.enums.TokenType;
import com.desmond.gadgetstore.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider{
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProviderImpl.class);

	@Value("${jwt.secret}")
    private String jwtSecret;
	
    private final TokenRepository tokenRepository;
    
    @Override
    public TokenEntity generateAccessToken(Map<String, Object> extraClaims, long duration, TemporalUnit durationType, UserEntity user) {
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(duration, durationType);

        String token = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expiryDate))
                .signWith(decodeSecretKey(jwtSecret), SignatureAlgorithm.HS256)
                .compact();
        
        var toBeSavedToken = TokenEntity.builder()
        		.user(user)
        		.type(TokenType.ACCESS)
        		.token(token)
        		.expiryDate(expiryDate)
        		.disabled(false)
        		.build();
        
        return tokenRepository.save(toBeSavedToken);
    }
    
    @Override
    public TokenEntity generateRefreshToken(long duration, TemporalUnit durationType, UserEntity user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(duration, durationType);

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expiryDate))
                .signWith(decodeSecretKey(jwtSecret), SignatureAlgorithm.HS256)
                .compact();

        var toBeSavedToken = TokenEntity.builder()
        		.user(user)
        		.type(TokenType.REFRESH)
        		.token(token)
        		.expiryDate(expiryDate)
        		.disabled(false)
        		.build();
        
        return tokenRepository.save(toBeSavedToken);
    }
    
    @Override
    public boolean validateToken(String tokenValue) {
        if(tokenValue == null)
            return false;
        try {
            Jwts.parser()
                    .setSigningKey(decodeSecretKey(jwtSecret))
                    .build()
                    .parseClaimsJws(tokenValue);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
          } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
          } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
          } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
          }
            return false;
        
    }
    
    @Override
    public String getUsernameFromToken(String tokenValue) {
        return extractClaim(tokenValue, Claims::getSubject);
    }
    
    @Override
    public LocalDateTime getExpiryDateFromToken(String tokenValue) {
        return toLocalDateTime(extractClaim(tokenValue, Claims::getExpiration));
    }
    
    
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(decodeSecretKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    private SecretKey decodeSecretKey(String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }
    
    
    private Date toDate(LocalDateTime localDateTime) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        return Date.from(localDateTime.toInstant(zoneOffset));
    }
    
    private LocalDateTime toLocalDateTime(Date date) {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        return date.toInstant().atOffset(zoneOffset).toLocalDateTime();
    }
}
