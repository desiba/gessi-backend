package com.desmond.gadgetstore.repositories;

import com.desmond.gadgetstore.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {
    boolean existsByName(String name);
}
