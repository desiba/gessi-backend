package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.common.utils.Helper;
import com.desmond.gadgetstore.common.utils.S3Service;
import com.desmond.gadgetstore.dtos.ProductListDto;
import com.desmond.gadgetstore.dtos.ProductListFilterDto;
import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.entities.PricingEntity;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.entities.ProductImageEntity;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.ProductCreateRequest;
import com.desmond.gadgetstore.payload.request.ProductUpdateRequest;
import com.desmond.gadgetstore.payload.response.BrandResponse;
import com.desmond.gadgetstore.repositories.BrandRepository;
import com.desmond.gadgetstore.repositories.CategoryRepository;
import com.desmond.gadgetstore.repositories.ProductImageRepository;
import com.desmond.gadgetstore.repositories.ProductRepository;
import com.desmond.gadgetstore.repositories.specifications.ProductSpecification;
import com.desmond.gadgetstore.services.BrandService;
import com.desmond.gadgetstore.services.CategoryService;
import com.desmond.gadgetstore.services.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;
    private final S3Service s3Service;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    public ProductEntity create(ProductCreateRequest request) {
    	
        Optional<BrandEntity> brand = brandRepository.findById(request.getBrandId());
        
        CategoryEntity category = categoryService.findById(request.getCategoryId());

        if (ObjectUtils.isEmpty(brand)) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_BRAND_NOT_FOUND);
        }

        if (ObjectUtils.isEmpty(category)) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_CATEGORY_NOT_FOUND);
        }
        
        long productCount = productRepository.count();

        ProductEntity product = ProductEntity.builder()
                .name(request.getName())
                .code(Helper.generateProductCode(
                		brand.get().getName(), 
                		category.getName(), 
                		productCount))
                .mainImage(request.getUrl())
                .category(category)
                .brand(brand.get())
                .mainImage(request.getUrl())
                .description(request.getDescription())
                .weight(request.getWeight())
                .build();
        
        product = productRepository.save(product);
        
        productRepository.updateSearchVector(product.getId());
        		
        return product;
    }

    @Override
    public ProductEntity update(UUID id, ProductUpdateRequest request) {
    	
    	BrandEntity brand = brandRepository
        		.findById(request.getBrandId())
        		.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_BRAND_NOT_FOUND));
    	
    	CategoryEntity category = categoryRepository
        		.findById(request.getCategoryId())
        		.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_CATEGORY_NOT_FOUND));
    	
        ProductEntity product = productRepository
        		.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.ERROR_PRODUCT_NOT_FOUND));
    	
    	
        product.setBrand(brand);
        product.setCategory(category);
        product.setName(request.getName());
        
        return productRepository.save(product);
    }

    @Override
    public ProductEntity findById(UUID productId) {
        Optional<ProductEntity> product = productRepository.findById(productId);
        
        if (product.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return product.get();
    }

    @Override
    public Page<ProductEntity> findProducts(ProductListDto request) {
    	
    	ProductListFilterDto filterDto = ProductListFilterDto.builder()
                .productName(request.getProductName())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .build();
    	
       
    	List<Sort.Order> orders = new ArrayList<>();
    	
    	orders.add(new Order(Direction.ASC, "createdAt"));
    	
    	Pageable pageRequest = PageRequest.of(
    			request.getPageNumber(), 
    			request.getPageSize(),
    			Sort.by(orders));
    	
        Specification<ProductEntity> specification = ProductSpecification.getSpecification(filterDto);
        

        Page<ProductEntity> result = productRepository.findAll(specification, pageRequest);
      
       return result;
        
    }

    @Override
    public ProductImageEntity saveProductImage(MultipartFile file) {
        String key = s3Service.multipartUploadFile(file);
        String url = "https://gadget-store-products.s3.us-east-1.amazonaws.com/" + key;
        var productImage = ProductImageEntity.builder()
                .description(file.getOriginalFilename())
                .name(file.getName())
                .url(url)
                .build();
        return productImageRepository.save(productImage);
    }
    
    private void getPricing(List<PricingEntity> pricies) {
    	
    }

}
