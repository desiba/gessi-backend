package com.desmond.gadgetstore.payload.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String baseUrl;
	private String origin;
	private String thumbnail;
	
}
