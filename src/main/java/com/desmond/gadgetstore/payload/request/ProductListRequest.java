package com.desmond.gadgetstore.payload.request;

import org.springdoc.core.annotations.ParameterObject;

import com.desmond.gadgetstore.dtos.ProductListDto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Schema
@Builder
@AllArgsConstructor
@ParameterObject
public class ProductListRequest {
	@Parameter(
			name = "productName", 
			description = "Name of the product",
			example = ""
			)
	private String productName;
	@Parameter(
			name = "brandId", 
			description = "Brand id",
			example = ""
			)
	private String brandId;
	@Parameter(
			name = "categoryId", 
			description = "Category Id", 
			example = "")
	private String categoryId;
	
	@Parameter(
			name = "searchTerm", 
			description = "search term", 
			example = "")
	private String searchTerm;
	
	private Integer page;
	private Integer size;
}
