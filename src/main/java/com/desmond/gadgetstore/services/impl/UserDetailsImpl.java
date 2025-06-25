package com.desmond.gadgetstore.services.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.desmond.gadgetstore.entities.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private UUID id;

	private String email;

	private String password;

	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpl(UUID id, String email, String password,
		      Collection<? extends GrantedAuthority> authorities) {
		    this.id = id;
		    this.email = email;
		    this.password = password;
		    this.authorities = authorities;
	}
	
	public static UserDetailsImpl build(UserEntity user) {
	    List<GrantedAuthority> authorities = user.getRoles().stream()
	        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
	        .collect(Collectors.toList());

	    return new UserDetailsImpl(
	        user.getId(), 
	        user.getEmail(),
	        user.getPassword(), 
	        authorities);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
		
		return null;
	}

}
