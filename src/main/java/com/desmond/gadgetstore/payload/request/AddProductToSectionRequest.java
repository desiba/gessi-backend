package com.desmond.gadgetstore.payload.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class AddProductToSectionRequest {
	@NotNull(message = "product id is required")
	private UUID productId;
	@NotNull(message = "section id is required")
	private UUID sectionId;
}



