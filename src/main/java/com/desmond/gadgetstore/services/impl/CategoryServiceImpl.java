package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.exceptions.ConstraintViolationException;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.CreateCategoryRequest;
import com.desmond.gadgetstore.repositories.CategoryRepository;
import com.desmond.gadgetstore.services.CategoryService;
import com.desmond.gadgetstore.services.Pageable;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryEntity create(CreateCategoryRequest request) {
        CategoryEntity category = categoryRepository.findOneByNameIgnoreCase(request.getName());
        if (category != null) {
            throw new ConstraintViolationException("Category name already exist");
        }
        var newCategory = CategoryEntity.builder()
                .name(request.getName())
                .image(request.getImageUrl())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public CategoryEntity findById(UUID id) {
        Optional<CategoryEntity> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found");
        }
        return category.get();
    }

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity update() {
        return null;
    }
}
