package com.desmond.gadgetstore.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.TokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {
	List<TokenEntity> findAllByUser(UserEntity user);
}
