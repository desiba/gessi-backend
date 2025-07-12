package com.desmond.gadgetstore.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.CartItemEntity;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItemEntity, UUID> {
	Optional<CartItemEntity> findByIdAndCartId(UUID id, UUID cartId);
}
