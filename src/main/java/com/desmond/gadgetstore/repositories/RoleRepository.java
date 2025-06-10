package com.desmond.gadgetstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.RoleEntity;
import com.desmond.gadgetstore.entities.RoleEnum;
import com.desmond.gadgetstore.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(RoleEnum name);
}
