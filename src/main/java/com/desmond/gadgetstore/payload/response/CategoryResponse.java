package com.desmond.gadgetstore.payload.response;

import java.util.UUID;

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
public class CategoryResponse {
	@JsonProperty("id")
    private UUID id;
	@JsonProperty("name")
    private String name;
	@JsonProperty("image")
    private String image;
}
