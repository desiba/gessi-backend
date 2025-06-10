package com.desmond.gadgetstore.repositories;

import com.desmond.gadgetstore.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    CategoryEntity findOneByNameIgnoreCase(String name);
}
