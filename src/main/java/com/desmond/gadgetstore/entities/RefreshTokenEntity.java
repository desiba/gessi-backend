package com.desmond.gadgetstore.entities;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "refreshtoken")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UserEntity user;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@Column(nullable = false)
	private Instant expiryDate;

	@UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date updatedAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;
	
}
