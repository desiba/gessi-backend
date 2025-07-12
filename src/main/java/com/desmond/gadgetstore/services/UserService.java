package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.UpdateUser;
import com.desmond.gadgetstore.payload.response.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse update(UUID id, UpdateUser request);

    UserEntity loggedIn();

    void delete(UserEntity user);

    List<UserEntity> findMany();

    Optional<UserEntity> findById(UUID userId);
    
    UserEntity getUserLoggedInfo();
}
