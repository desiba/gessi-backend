package com.desmond.gadgetstore.payload.request;

import jakarta.validation.constraints.NotNull;

public class CreateProduct {
	@NotNull()
    private String name;
    private String code;
    private String description;

    @NotNull()
    private double price;
    @NotNull()
    private int quantity;

    private float weight;
}
