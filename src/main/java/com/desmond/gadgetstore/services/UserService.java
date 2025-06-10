package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.payload.request.UpdateUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserEntity update(UpdateUser request);

    UserEntity loggedIn();

    void delete(UserEntity user);

    List<UserEntity> findMany();

    Optional<UserEntity> findById(UUID userId);
}
