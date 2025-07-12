package com.desmond.gadgetstore.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.common.utils.S3Service;
import com.desmond.gadgetstore.entities.BannerEntity;
import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.entities.SectionEntity;
import com.desmond.gadgetstore.entities.SectionProductEntity;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ConstraintViolationException;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.AddProductToSectionRequest;
//import com.desmond.gadgetstore.mapper.SectionMapper;
import com.desmond.gadgetstore.payload.request.BannerRequest;
import com.desmond.gadgetstore.payload.request.CreateSectionRequest;
import com.desmond.gadgetstore.payload.response.BannerResponse;
import com.desmond.gadgetstore.payload.response.CategoryResponse;
import com.desmond.gadgetstore.payload.response.HomeResponse;
import com.desmond.gadgetstore.payload.response.ProductResponse;
import com.desmond.gadgetstore.payload.response.SectionResponse;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.repositories.BannerRepository;
import com.desmond.gadgetstore.repositories.BrandRepository;
import com.desmond.gadgetstore.repositories.CategoryRepository;
import com.desmond.gadgetstore.repositories.ProductImageRepository;
import com.desmond.gadgetstore.repositories.ProductRepository;
import com.desmond.gadgetstore.repositories.SectionProductRepository;
import com.desmond.gadgetstore.repositories.SectionRepository;
import com.desmond.gadgetstore.services.BrandService;
import com.desmond.gadgetstore.services.CategoryService;
import com.desmond.gadgetstore.services.HelperService;
import com.desmond.gadgetstore.services.ProductService;
import com.desmond.gadgetstore.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelperServiceImpl implements HelperService {
	private final BannerRepository bannerRepository;
	
	private final ProductService productService;
	private final CategoryService categoryService;
	private final SectionRepository sectionRepository;
	private final SectionProductRepository sectionProductRepository;
	private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandService brandService;

	@Override
	public List<BannerResponse> getBanners() {
		List<BannerEntity> banners = bannerRepository.findAll();
		List<BannerResponse> response = new ArrayList<>();

		for(BannerEntity banner : banners) {

			var resp = BannerResponse
					.builder()
					.category(banner.getCategory())
					.image(banner.getImage())
					.product(banner.getProduct())
					.build();

			response.add(resp);
		}
		return response;
	}

	@Override
	public void createBanner(BannerRequest request) {
		var product = productService.findById(request.getProductId());
		var category = categoryService.findById(request.getCategoryId());

		var banner = BannerEntity.builder()
				.category(category)
				.image(request.getImage())
				.product(product)
				.build();

		bannerRepository.save(banner);
	}

	@Override
	public List<SectionEntity> getSections() {
		return sectionRepository.findAll();
	}

	@Override
	public void addProductToSection(AddProductToSectionRequest request) {
		findSectionById(request.getSectionId());
		var product = productService.findById(request.getProductId());
		product.setSectionId(request.getSectionId());
		productRepository.save(product);
	}


	@Override
	public void removeProductFromSection(UUID productId) {
		productService.findById(productId);
		sectionProductRepository.deleteByProductId(productId);
	}


	@Override
	public void deleteSectionById(UUID id) {
		sectionRepository.deleteById(id);
	}

	@Override
	public void createSection(CreateSectionRequest request) {
		var section = SectionEntity.builder()
				.title(request.getTitle())
				.type(request.getType())
				.build();
		sectionRepository.save(section);
	}
	
	private SectionEntity findSectionById(UUID sectionId) {
		var section = sectionRepository.findById(sectionId);
		if (section.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ERROR_SECTION_NOT_FOUND);
		}
		return section.get();
	}
	
	private List<CategoryResponse> categories (){
		List<CategoryResponse> categoryResponses = new ArrayList<>();
		var categories = categoryRepository.findAll();
		for(CategoryEntity res: categories) {
			var catResponse = CategoryResponse.builder()
					.id(res.getId())
					.name(res.getName())
					.image(res.getImage())
					.productCount(0)
					.build();
			categoryResponses.add(catResponse);
		}
		return categoryResponses;
	}

	@Override
	public HomeResponse getHomeResponse() {
				
		UserResponse userResponse = UserResponse.builder()
					.id(UUID.randomUUID())
					.firstName("John")
					.lastName("Doe")
					.isActive(false)
					.isEmailValid(false)
					.build();;
		
		return HomeResponse.builder()
				.banners(getBanners())
				.categories(categories())
				.sections(getSections())
				.brands(brandService.findAll())
				.notificationCount(5)
				.user(userResponse)
				.build();
	}


	

}
