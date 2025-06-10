package com.desmond.gadgetstore.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desmond.gadgetstore.common.utils.ErrorMessages;
import com.desmond.gadgetstore.entities.BannerEntity;
import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.entities.SectionEntity;
import com.desmond.gadgetstore.entities.SectionProductEntity;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.mapper.SectionMapper;
import com.desmond.gadgetstore.payload.request.BannerRequest;
import com.desmond.gadgetstore.payload.request.CreateSectionRequest;
import com.desmond.gadgetstore.payload.response.BannerResponse;
import com.desmond.gadgetstore.payload.response.CategoryResponse;
import com.desmond.gadgetstore.payload.response.HomeResponse;
import com.desmond.gadgetstore.payload.response.ProductResponse;
import com.desmond.gadgetstore.payload.response.SectionResponse;
import com.desmond.gadgetstore.payload.response.UserResponse;
import com.desmond.gadgetstore.repositories.BannerRepository;
import com.desmond.gadgetstore.repositories.CategoryRepository;
import com.desmond.gadgetstore.repositories.SectionProductRepository;
import com.desmond.gadgetstore.repositories.SectionRepository;
import com.desmond.gadgetstore.services.CategoryService;
import com.desmond.gadgetstore.services.HelperService;
import com.desmond.gadgetstore.services.ProductService;
import com.desmond.gadgetstore.services.UserService;

@Service
public class HelperServiceImpl implements HelperService {
	private final BannerRepository bannerRepository;
	private final ProductService productService;
	private final CategoryService categoryService;
	private final SectionRepository sectionRepository;
	private final SectionProductRepository sectionProductRepository;
    private final SectionMapper sectionMapper;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    

	public HelperServiceImpl(
			BannerRepository bannerRepository,
			ProductService productService,
			CategoryService categoryService,
			SectionRepository sectionRepository,
			SectionMapper sectionMapper,
			SectionProductRepository sectionProductRepository,
			CategoryRepository categoryRepository,
			UserService userService
			) {
        this.bannerRepository = bannerRepository;
        this.productService = productService;
        this.categoryService = categoryService;
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
        this.sectionProductRepository = sectionProductRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }


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
	public void addProductToSection(UUID sectionId, UUID productId) {
		findSectionById(sectionId);
		var product = productService.findById(productId);
		 
		var toCreateSection = SectionProductEntity.builder()
		.sectionId(sectionId)
		.product(product)
		.build();
		System.out.println(toCreateSection);
		sectionProductRepository.save(toCreateSection);
	}


	@Override
	public void removeProductFromSection(UUID productId) {
		productService.findById(productId);
		sectionProductRepository.deleteByProductId(productId);
	}


	@Override
	public void deleteSectionById(UUID id) {
		// TODO Not completed
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
					.build();
			categoryResponses.add(catResponse);
		}
		return categoryResponses;
	}

	@Override
	public HomeResponse getHomeResponse() {
		//var user = userService.loggedIn();
		UserResponse userResponse = UserResponse.builder()
				.id(UUID.randomUUID())
				.firstName("John")
				.lastName("Doe")
				.isActive(false)
				.isEmailValid(false)
				.build();;
		/*
		if(user != null) {
			userResponse = UserResponse.builder()
					.id(user.getId())
					.firstName(user.getFirstName())
					.lastName(user.getLastName())
					.isActive(user.isActive())
					.isEmailValid(user.isEmailValid())
					.build();
		}else {
			userResponse = UserResponse.builder()
					.id(UUID.randomUUID())
					.firstName("John")
					.lastName("Doe")
					.isActive(false)
					.isEmailValid(false)
					.build();
		}
		*/
		return HomeResponse.builder()
				.banners(getBanners())
				.categories(categories())
				.sections(getSections())
				.notificationCount(5)
				.user(userResponse)
				.build();
	}

}
