package com.desmond.gadgetstore.dtos;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
@AllArgsConstructor
@ParameterObject
public class ProductListDto {
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
	private UUID brandId;
	@Parameter(
			name = "categoryId", 
			description = "Category Id", 
			example = "")
	private UUID categoryId;
	
	@Parameter(
			name = "searchTerm", 
			description = "search term", 
			example = "")
	private String searchTerm;
	
	@Parameter(
			name = "pageNumber", 
			description = "page number", 
			example = "0")
	private int pageNumber;
	
	@Parameter(
			name = "pageSize", 
			description = "page number",
			example = "5")
	private int pageSize;
}
