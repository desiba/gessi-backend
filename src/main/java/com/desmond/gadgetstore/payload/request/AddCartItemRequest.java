package com.desmond.gadgetstore.payload.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCartItemRequest {
	@NotNull(message = "product id is required")
	private UUID productId;
	@Positive()
	private int quantity;
}
