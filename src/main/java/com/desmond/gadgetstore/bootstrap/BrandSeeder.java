package com.desmond.gadgetstore.bootstrap;

import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.RoleEntity;
import com.desmond.gadgetstore.entities.RoleEnum;
import com.desmond.gadgetstore.repositories.BrandRepository;
import com.desmond.gadgetstore.repositories.RoleRepository;

@Component
public class BrandSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final BrandRepository brandRepository;
    
    public BrandSeeder (BrandRepository brandRepository) {
    	this.brandRepository = brandRepository;
    }

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		 this.createBrands();
	}

	private void createBrands() {
		var brandDto = BrandEntity.builder()
				.name("")
				.imageUrl("")
				.build();
		
       // Optional<BrandEntity> optionalRole = brandRepository.findByName();

		
	}

}
