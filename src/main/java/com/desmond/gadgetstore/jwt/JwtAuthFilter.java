package com.desmond.gadgetstore.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.desmond.gadgetstore.common.utils.Constants.TOKEN_HEADER;
import static com.desmond.gadgetstore.common.utils.Constants.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	@Value("${jwt.access.cookie-name}")
    private String accessTokenCookieName;
	private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal
            (@NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    	
		String accessToken  = getJwtToken(request, true);
	    if(accessToken == null || !tokenProvider.validateToken(accessToken)) {
	            filterChain.doFilter(request, response);
	            return;
	    }

		String username = tokenProvider.getUsernameFromToken(accessToken);
		 
	    if(username == null) {
	            filterChain.doFilter(request, response);
	            return;
	    }
    	
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
	    
	    filterChain.doFilter(request, response);
    }
    
    private String getJwtToken(HttpServletRequest request, boolean fromCookie) {
        if (fromCookie) return getJwtFromCookie(request);
        return getJwtFromRequest(request);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies == null)
            return null;

        for (Cookie cookie : cookies) {
            if (accessTokenCookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
