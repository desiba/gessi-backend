package com.desmond.gadgetstore.entities;

import java.util.Date;
import java.util.UUID;


import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pricing")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	private UUID productId;
			
	@ColumnDefault("0.0")
    private double price;
	
	@UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date updatedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

}
