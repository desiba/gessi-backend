package com.desmond.gadgetstore.payload.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PricingResponse {
	private UUID id;
	private UUID productId;
	private double price;
}
