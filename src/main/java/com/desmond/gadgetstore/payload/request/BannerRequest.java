package com.desmond.gadgetstore.payload.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BannerRequest {
	@NotNull()
	private UUID productId;
	@NotNull()
	private UUID categoryId;
	@NotNull()
	private String image;
}
