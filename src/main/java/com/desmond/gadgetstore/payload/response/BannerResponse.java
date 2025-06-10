package com.desmond.gadgetstore.payload.response;

import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponse {
	
	@JsonProperty("image")
    private String image;
    @JsonProperty("category")
    private CategoryEntity category;
    @JsonProperty("product")
    private ProductEntity product;

}
