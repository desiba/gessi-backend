package com.desmond.gadgetstore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.model.JSONType;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "carts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @OneToMany(
    		fetch = FetchType.LAZY,
    		cascade = CascadeType.PERSIST,
    		orphanRemoval = true)
    private Set<CartItemEntity> items;

    @OneToOne
    private UserEntity user;

    private double totalAmount;

    private double subTotal;

    private double taxes = 0.0;

    private double vats = 0.0;

    private double discount = 0.0;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date updatedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;
}
