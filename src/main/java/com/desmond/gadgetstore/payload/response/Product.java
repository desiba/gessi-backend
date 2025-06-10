package com.desmond.gadgetstore.payload.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private UUID id;

    private String name;
    private String code;
    
    private PricingResponse pricing;
    
    private String[] otherImages;
    
    private String mainImage;

    private String description;
    private float weight;
}
