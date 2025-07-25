package com.desmond.gadgetstore.payload.response;

import java.util.List;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.SectionEntity;
import com.desmond.gadgetstore.entities.SectionProductEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponse {
	
	@JsonProperty("banners")
    private List<BannerResponse> banners;
    @JsonProperty("categories")
    private List<CategoryResponse> categories;
    @JsonProperty("sections")
    private List<SectionEntity> sections;
    @JsonProperty("brands")
    private List<BrandResponse> brands;
    @JsonProperty("user")
    private UserResponse user;
    @JsonProperty("notificationCount")
    private int notificationCount;
    

}
