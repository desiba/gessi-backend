package com.desmond.gadgetstore.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
	 @Override
	 @Transactional
	 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		 
	    UserEntity user = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
	    
	    return UserDetailsImpl.build(user);
	 }

}
