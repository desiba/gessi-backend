package com.desmond.gadgetstore.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.RefreshTokenEntity;
import com.desmond.gadgetstore.entities.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
	Optional<RefreshTokenEntity> findByToken(String token);

	@Modifying
	int deleteByUser(UserEntity user);
}


