package com.desmond.gadgetstore.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "section-products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionProductEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	private UUID sectionId;
	
	@ManyToOne
	private ProductEntity product;

}
