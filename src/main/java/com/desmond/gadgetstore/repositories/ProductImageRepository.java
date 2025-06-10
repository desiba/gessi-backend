package com.desmond.gadgetstore.repositories;

import com.desmond.gadgetstore.entities.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity, UUID> {
}
