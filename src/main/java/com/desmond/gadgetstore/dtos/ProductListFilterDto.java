package com.desmond.gadgetstore.dtos;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ParameterObject
public class ProductListFilterDto {
	
	private String productName;
	
	private UUID brandId;
	
	private UUID categoryId;
	
	private String searchTerm;

}
