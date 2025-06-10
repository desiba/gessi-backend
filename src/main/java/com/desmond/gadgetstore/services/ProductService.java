package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.dtos.ProductListDto;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.entities.ProductImageEntity;
import com.desmond.gadgetstore.payload.request.ProductCreateRequest;
import com.desmond.gadgetstore.payload.request.ProductUpdateRequest;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductService {
    ProductEntity create(ProductCreateRequest request);

    ProductEntity update(UUID id, ProductUpdateRequest request);

    ProductEntity findById(UUID productId);

    Page<ProductEntity> findProducts(ProductListDto filter);

    ProductImageEntity saveProductImage(MultipartFile file);
}
