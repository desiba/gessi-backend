package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.payload.request.CreateBrandRequest;
import com.desmond.gadgetstore.payload.response.BrandResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandService {
	BrandResponse create(CreateBrandRequest request);

    BrandResponse findById(UUID id);
    
    boolean isExist(UUID id);

    List<BrandResponse> findAll();

    BrandResponse update();
    
    
}
