package com.desmond.gadgetstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVectorRequest {
	private String name;
	private String description;
	private String code;
}
