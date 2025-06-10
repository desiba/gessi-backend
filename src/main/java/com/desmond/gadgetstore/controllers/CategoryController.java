package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.payload.request.CreateCategoryRequest;
import com.desmond.gadgetstore.services.CategoryService;

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
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public ResponseEntity<CategoryEntity> create(
    		@Valid @RequestBody CreateCategoryRequest request
    		) {
        CategoryEntity createdCategory = categoryService.create(request);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }
    
    @GetMapping()
    public ResponseEntity<List<CategoryEntity>> list(){
    	List<CategoryEntity> categories = categoryService.findAll();
    	return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<CategoryEntity> findById(){
    	return null;
    }
    
    
}
