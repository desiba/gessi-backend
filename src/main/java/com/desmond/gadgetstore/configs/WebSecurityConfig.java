package com.desmond.gadgetstore.configs;

import com.desmond.gadgetstore.jwt.JwtAuthEntryPoint;
import com.desmond.gadgetstore.jwt.JwtAuthFilter;
import com.desmond.gadgetstore.jwt.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/actuator/**"
    };
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:9001"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http.csrf(AbstractHttpConfigurer::disable);
    	
    	 http
         .cors(cors -> cors.configurationSource(request -> {
             CorsConfiguration configuration = new CorsConfiguration();
             configuration.setAllowedOrigins(Collections.singletonList("http://localhost:9001"));
             configuration.setAllowedMethods(Collections.singletonList("*"));
             configuration.setAllowCredentials(true);
             configuration.setAllowedHeaders(Collections.singletonList("*"));
             configuration.setMaxAge(3600L);
             return configuration;
         }));
    	 
    	 http
         .authorizeHttpRequests(authorize -> {
        	 
             authorize.requestMatchers(SWAGGER_WHITELIST).permitAll();
             authorize.requestMatchers("/api/v1/auth/**").permitAll();
             authorize.requestMatchers("/api/v1/helper/**").permitAll();
             authorize.requestMatchers("/api/v1/products/**").permitAll();
             authorize.requestMatchers("/api/v1/categories/**").permitAll();
             
             //authorize.requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("");
             //authorize.requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority(Permissions.USER_CREATE.getName());
             //authorize.requestMatchers(HttpMethod.PUT, "/api/users/**").hasAuthority(Permissions.USER_UPDATE.getName());
             //authorize.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority(Permissions.USER_DELETE.getName());
             //authorize.anyRequest().permitAll();
             
             authorize.anyRequest().authenticated();
         });
    	 
    	 http
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    	 
    	 http
         .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint));
    	 
    	 http
         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    	 
    	 return http.build();
                
    }
}
