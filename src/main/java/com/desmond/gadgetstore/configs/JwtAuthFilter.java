package com.desmond.gadgetstore.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.RefreshTokenService;
import com.desmond.gadgetstore.services.impl.UserDetailsServiceImpl;

import java.io.IOException;

@Service

public class JwtAuthFilter extends OncePerRequestFilter {
	@Autowired
    private JwtService jwtService;
	@Autowired
    private UserDetailsService userDetailsService;
    
    
    @Override
    protected void doFilterInternal
            (@NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    	
    	try {

	        //final String authHeader = request.getHeader("Authorization");
    		 String jwt = parseJwt(request);
    		/*
	       
	        
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	        }
	
	        jwt = authHeader.substring(7);
	
	        email = jwtService.extractUsername(jwt);
	        */
	        if (jwt != null && jwtService.validateJwtToken(jwt)) {
	        	
	        	String username = jwtService.getUserNameFromJwtToken(jwt);
	        	
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	            
	            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                    userDetails,
	                    null,
	                    userDetails.getAuthorities()
	            );
	            
	            authToken.setDetails(
	                    new WebAuthenticationDetailsSource().buildDetails(request)
	            );
	            
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	        }
        
    	} catch(Exception e) {
    		 logger.error("Cannot set user authentication: {}", e);
    	}
        filterChain.doFilter(request, response);
    }
    
    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtService.getJwtFromCookies(request);
        return jwt;
     }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/api/v1/auth");
    }

}
