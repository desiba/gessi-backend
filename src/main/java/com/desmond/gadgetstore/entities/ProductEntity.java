package com.desmond.gadgetstore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.desmond.gadgetstore.payload.request.ProductImageData;

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;
    private String code;
    private String model;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productId")
    private List<PricingEntity> pricing;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productId")
    private List<ProductImageEntity> images;
    
    private String mainImage;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private double weight;

    @ManyToOne(fetch = FetchType.EAGER)
    private BrandEntity brand;
    
    private UUID sectionId;

    @ManyToOne(fetch = FetchType.EAGER)
    private CategoryEntity category;

    @Type(PostgreSQLTSVectorType.class)
    @Column(name = "search_vector", columnDefinition = "tsvector", nullable = true)
    private String searchVector;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date updatedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;
    

}
