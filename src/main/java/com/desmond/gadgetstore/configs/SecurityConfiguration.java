package com.desmond.gadgetstore.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.desmond.gadgetstore.common.utils.Role;
import com.desmond.gadgetstore.common.utils.UserRole;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources"
    };
    
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthFilter jwtAuthFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        return http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/v1/auth/*").permitAll()
                        		
                        		.requestMatchers("/api/v1/admin/*").hasRole(UserRole.ADMIN.getRole())
                        		
                        		.requestMatchers("/api/v1/owner/*").hasRole(UserRole.ADMIN.getRole())
                        		
                        		.requestMatchers("/api/v1/products/*").permitAll()
                        		
                        		.requestMatchers("/api/v1/helper/*").permitAll()  
                        		
                                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        		                                
                                .requestMatchers("/api/v1/users/*").authenticated()
                         
                                .anyRequest().authenticated()
                )

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
                
    }

}
