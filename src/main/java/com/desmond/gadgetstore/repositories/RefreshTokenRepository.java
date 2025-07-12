package com.desmond.gadgetstore.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.TokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<TokenEntity, UUID> {
	Optional<TokenEntity> findByToken(String token);
	
	Optional<TokenEntity> findByUserId(UUID userId);

	@Modifying
	int deleteByUser(UserEntity user);
	
	
}


