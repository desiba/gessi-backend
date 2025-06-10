package com.desmond.gadgetstore.payload.response;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.desmond.gadgetstore.common.utils.SectionType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponse {
	@JsonProperty("id")
    private UUID id;
    @JsonProperty("products")
    private Set<ProductResponse> products;
    @JsonProperty("title")
    private String title;
    @JsonProperty("type")
    private SectionType type;
    
}
