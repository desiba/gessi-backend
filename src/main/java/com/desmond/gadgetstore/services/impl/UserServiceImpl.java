package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.UpdateUser;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public UserResponse update(UUID id, UpdateUser request) {
    	
    	var user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_USER_NOT_FOUND));

    	user.setFirstName(request.getFirstName());
    	user.setLastName(request.getLastName());
    	
    	UserEntity updatedUser = modelMapper.map(request, UserEntity.class);
    	
        return null;
    }

    @Override
    public UserEntity loggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

    @Override
    public void delete(UserEntity user) {
        userRepository.delete(user);
    }

    @Override
    public List<UserEntity> findMany() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        return userRepository.findById(userId);
    }
    
    public UserEntity getUserLoggedInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return userRepository.findById(userDetails.getId()).get();
	}

}
