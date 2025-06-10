package com.desmond.gadgetstore.repositories;

import com.desmond.gadgetstore.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    CartEntity findByUserId(UUID userId);
}
