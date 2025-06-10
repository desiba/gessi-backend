package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.payload.request.CreateCategoryRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    CategoryEntity create(CreateCategoryRequest request);

    CategoryEntity findById(UUID id);

    List<CategoryEntity> findAll();

    CategoryEntity update();
}
