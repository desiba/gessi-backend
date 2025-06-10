package com.desmond.gadgetstore.entities;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.desmond.gadgetstore.common.utils.SectionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sections")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	 @OneToMany(
	    		fetch = FetchType.EAGER,
	    		cascade = CascadeType.PERSIST,
	    		orphanRemoval = true)
	 private Set<SectionProductEntity> products;
	 
	 private String title;
	 private SectionType type;
	 
	@UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date updatedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;
}
