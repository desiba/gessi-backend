package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.common.utils.S3Service;
import com.desmond.gadgetstore.dtos.ProductListDto;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.entities.ProductImageEntity;
import com.desmond.gadgetstore.payload.request.ProductCreateRequest;
import com.desmond.gadgetstore.payload.request.ProductUpdateRequest;
import com.desmond.gadgetstore.services.ProductService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<ProductEntity> create(@Valid @RequestBody ProductCreateRequest request) {
        ProductEntity createdProduct = productService.create(request);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PostMapping(path = "upload-image", consumes = "multipart/form-data")
    public ResponseEntity<ProductImageEntity> uploadImage(@RequestParam("image") MultipartFile file) {
        ProductImageEntity productImage = productService.saveProductImage(file);
        return new ResponseEntity<>(productImage, HttpStatus.OK);
    }
    
    @GetMapping()
    public ResponseEntity<Page<ProductEntity>> list(ProductListDto filter){
    	Page<ProductEntity> products = productService.findProducts(filter);
    	return ResponseEntity.ok(products);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<ProductEntity> findById(
    		@Parameter(description = "product id", required = true) @Valid @PathVariable("id") UUID productId
    		){
    	ProductEntity products = productService.findById(productId);
    	return new ResponseEntity<>(products, HttpStatus.OK);
    }
    
    @PutMapping("{id}")
    public ResponseEntity<ProductEntity> update(
    		@Parameter(description = "product id", required = true) @Valid @PathVariable("id") UUID productId,
    		@Valid @RequestBody ProductUpdateRequest request
    		){
    	ProductEntity updatedProduct = productService.update(productId, request);
    	return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
    
}
