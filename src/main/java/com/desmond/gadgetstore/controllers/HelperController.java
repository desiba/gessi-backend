package com.desmond.gadgetstore.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.SectionEntity;
import com.desmond.gadgetstore.entities.SectionProductEntity;
import com.desmond.gadgetstore.payload.request.AddProductToSectionRequest;
import com.desmond.gadgetstore.payload.request.BannerRequest;
import com.desmond.gadgetstore.payload.request.CreateSectionRequest;
import com.desmond.gadgetstore.payload.response.HomeResponse;
import com.desmond.gadgetstore.services.HelperService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/helper")
public class HelperController {
	
     private final HelperService helperService;

	 public HelperController(HelperService helperService) {
		this.helperService = helperService;
	 }
		
	 @GetMapping("home")
	 public ResponseEntity<HomeResponse> home() {
	    HomeResponse homeResponse = helperService.getHomeResponse();
	    return new ResponseEntity<>(homeResponse, HttpStatus.OK);
	 }
	 
	 @PostMapping("banners")
     public ResponseEntity<BrandEntity> createBanner(@Valid @RequestBody BannerRequest request) {
        helperService.createBanner(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
     }
	 
	 @GetMapping("sections")
	 public ResponseEntity<List<SectionEntity>> getSections() {
	    List<SectionEntity> sections = helperService.getSections();
	    return new ResponseEntity<>(sections, HttpStatus.OK);
	 }
	 
	 
	 @PutMapping("sections")
     public ResponseEntity<BrandEntity> addProductToSection(
     		@Valid
     		@RequestBody AddProductToSectionRequest request
    		 ) {
        helperService.addProductToSection(request);
        return new ResponseEntity<>(HttpStatus.OK);
     }
	 
	 @PostMapping("sections")
     public ResponseEntity<?> createSection(@Valid @RequestBody CreateSectionRequest request) {
        helperService.createSection(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
     }

}
