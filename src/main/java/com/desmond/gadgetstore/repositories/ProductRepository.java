package com.desmond.gadgetstore.repositories;

import com.desmond.gadgetstore.entities.ProductEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    @Query(value = "SELECT * FROM products where search_vector @@ to_tsquery(:searchTerm)", nativeQuery = true)
    List<ProductEntity> findProductsBySearch(@Param("searchTerm") String searchTerm);

    @Modifying
    @Query(value = 
    		"UPDATE products SET search_vector = setweight(to_tsvector('english', COALESCE(name, '')), 'A') || setweight(to_tsvector('english', COALESCE(description, '')), 'B') || setweight(to_tsvector('english', COALESCE(code, '')), 'C') WHERE id = :productId",
        nativeQuery = true)
    void updateSearchVector(UUID productId);
    
    Optional<ProductEntity> findByIdAndSectionId(UUID id, UUID sectionId);
    
}
