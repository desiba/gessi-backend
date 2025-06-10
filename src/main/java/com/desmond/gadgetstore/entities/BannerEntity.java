package com.desmond.gadgetstore.entities;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banners")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	private String image;
	
	@OneToOne()
	private CategoryEntity category;
	
	@OneToOne()
	private ProductEntity product;
	
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date updatedAt;
	

}
