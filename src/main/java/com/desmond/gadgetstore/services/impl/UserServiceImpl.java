package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.UpdateUser;
import com.desmond.gadgetstore.repositories.UserRepository;
import com.desmond.gadgetstore.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity update(UpdateUser request) {
        return null;
    }

    @Override
    public UserEntity loggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        

        UserEntity currentUser =   (UserEntity) authentication.getPrincipal();
        
        //System.out.println(currentUser);
        
        //Optional<UserEntity> user = userRepository.findByEmail(currentUser.getUsername());
        
        return currentUser;
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

}
