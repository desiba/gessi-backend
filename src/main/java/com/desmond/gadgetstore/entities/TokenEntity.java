package com.desmond.gadgetstore.entities;


import com.desmond.gadgetstore.enums.TokenType;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	@ManyToOne
	private UserEntity user;
	
	@Enumerated(EnumType.STRING)
    private TokenType type;
    private String token;
    private LocalDateTime expiryDate;
    private boolean disabled;
}
