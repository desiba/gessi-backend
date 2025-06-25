package com.desmond.gadgetstore.configs;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.services.impl.UserDetailsImpl;

import javax.crypto.SecretKey;

import java.util.*;
import java.util.function.Function;


@Component
public class JwtService {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);


	@Value("${TOKEN_SECRET}")
    private String SECRET;
	
	@Value("${jwt.jwtCookieName}")
	private String jwtCookie;
	
	@Value("${jwt.jwtRefreshCookieName}")
	private String jwtRefreshCookie;
	
	@Value("${jwt.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
		String jwt = generateTokenFromEmail(userPrincipal.getEmail());   
		return generateCookie(jwtCookie, jwt, "/api");
	}
  
	public ResponseCookie generateJwtCookie(UserEntity user) {
	   String jwt = generateTokenFromEmail(user.getEmail());   
	   return generateCookie(jwtCookie, jwt, "/api");
	}
	  
	public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
	   return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth/refreshtoken");
	}
	  
	public String getJwtFromCookies(HttpServletRequest request) {
	   return getCookieValueByName(request, jwtCookie);
	}
	  
	public String getJwtRefreshFromCookies(HttpServletRequest request) {
	    return getCookieValueByName(request, jwtRefreshCookie);
	}

	public ResponseCookie getCleanJwtCookie() {
	    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
	    return cookie;
	}
	  
	public ResponseCookie getCleanJwtRefreshCookie() {
	    ResponseCookie cookie = ResponseCookie.from(jwtRefreshCookie, null).path("/api/auth/refreshtoken").build();
	    return cookie;
	}
	
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", populateAuthorities(user.getAuthorities()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }
    
    public String generateTokenFromEmail(String email) {
    	 return Jwts.builder()
                 .setSubject(email)
                 .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                 .signWith(SignatureAlgorithm.HS256, getSigningKey())
                 .compact();
      }
    
    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value).path(path).maxAge(24 * 60 * 60).httpOnly(true).build();
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority: authorities) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    
    public boolean validateJwtToken(String authToken) {
        try {
        	Jwts.parser().setSigningKey(getSigningKey()).build();
        	
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
    
    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
          return cookie.getValue();
        } else {
          return null;
        }
    }
    
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build()
                   .parseClaimsJws(token).getBody().getSubject();
      }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
