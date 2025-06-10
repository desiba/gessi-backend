package com.desmond.gadgetstore.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.SectionProductEntity;

@Repository
public interface SectionProductRepository extends JpaRepository<SectionProductEntity, UUID>{
	void deleteByProductId(UUID productId);
}
