package com.desmond.gadgetstore.services;

import java.util.List;
import java.util.UUID;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.SectionEntity;
import com.desmond.gadgetstore.payload.request.BannerRequest;
import com.desmond.gadgetstore.payload.request.CreateSectionRequest;
import com.desmond.gadgetstore.payload.response.BannerResponse;
import com.desmond.gadgetstore.payload.response.HomeResponse;
import com.desmond.gadgetstore.payload.response.SectionResponse;

public interface HelperService {
	
	void createBanner(BannerRequest request);
	
	List<BannerResponse> getBanners();
	
	List<SectionEntity> getSections();
		
	void addProductToSection(UUID sectionId, UUID productId);
		
	void createSection(CreateSectionRequest request);
	
	void removeProductFromSection(UUID productId);
	
	void deleteSectionById(UUID id);
	
	HomeResponse getHomeResponse();
		
}
