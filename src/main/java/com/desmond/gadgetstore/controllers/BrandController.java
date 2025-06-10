package com.desmond.gadgetstore.controllers;


import com.desmond.gadgetstore.payload.request.CreateBrandRequest;
import com.desmond.gadgetstore.payload.response.BrandResponse;
import com.desmond.gadgetstore.services.BrandService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping()
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody CreateBrandRequest request) {
    	BrandResponse createdBrand = brandService.create(request);
        return new ResponseEntity<>(createdBrand, HttpStatus.CREATED);
    }
    
    @GetMapping()
    public ResponseEntity<List<BrandResponse>> list(){
    	List<BrandResponse> brands = brandService.findAll();
    	return new ResponseEntity<>(brands, HttpStatus.OK);
    }
}
