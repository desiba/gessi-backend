package com.desmond.gadgetstore.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
	
	@Schema(example= "washing machine", description = "product name")
    @NotEmpty()
    private String name;
	
	@Schema(example= "1000")
    @NotNull
    @Positive
    private float price;
	
	@Schema(example= "manual washing machine")
    @NotEmpty
    private String description;
	
	@Schema(example= "5")
    @NotNull
    @Positive
    private int quantity;
    
	@Schema(example= "10")
    @NotNull
    @Positive
    private float weight;
	
	@Schema(example= "https://gadget-store-products.s3.us-east-1.amazonaws.com/washing-machine.avif")
    @NotEmpty
    private String url;
    
    @Schema(example= "dad66c81-7724-4d23-9afb-2d620f46afa7")
    @NotNull
    private UUID categoryId;
    
    @Schema(example= "6d671a74-1c18-4a6f-9188-8b42964cc1ec")
    @NotNull
    private UUID brandId;
}
