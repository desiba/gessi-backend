package com.desmond.gadgetstore.payload.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	@JsonProperty("id")
	private UUID id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("code")
	private String code;
	@JsonProperty("newPrice")
	private double price;
	@JsonProperty("prevPrice")
	private double prevPrice;
}
