package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.CreateBrandRequest;
import com.desmond.gadgetstore.payload.response.BrandResponse;
import com.desmond.gadgetstore.repositories.BrandRepository;
import com.desmond.gadgetstore.services.BrandService;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    
    private final ModelMapper modelMapper;

    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BrandResponse create(CreateBrandRequest request) {
        var newBrand = BrandEntity.builder()
                .name(request.getName())
                .imageUrl(request.getImage())
                .build();
        var brandEntity = brandRepository.save(newBrand);
        return modelMapper.map(brandEntity, BrandResponse.class);
    }

    @Override
    public BrandResponse findById(UUID id) {
        Optional<BrandEntity> brand = brandRepository.findById(id);
        if(brand.isEmpty()) {
            throw new ResourceNotFoundException("Brand not found");
        }
        return modelMapper.map(brand.get(), BrandResponse.class);
    }

    @Override
    public boolean isExist(UUID id) {
        return brandRepository.existsById(id);
    }

    @Override
    public List<BrandResponse> findAll() {
    	
    	List<BrandResponse> brandResponses = new ArrayList<>();
    	
    	List<BrandEntity> brands= brandRepository.findAll();
    	
    	for(BrandEntity brandEntity : brands) {
    		brandResponses.add(modelMapper.map(brandEntity, BrandResponse.class));
    	}
    	
    	return brandResponses;
    }

    @Override
    public BrandResponse update() {
        return null;
    }
}
