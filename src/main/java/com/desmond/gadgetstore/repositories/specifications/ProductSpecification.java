package com.desmond.gadgetstore.repositories.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.desmond.gadgetstore.dtos.ProductListDto;
import com.desmond.gadgetstore.dtos.ProductListFilterDto;
import com.desmond.gadgetstore.entities.BrandEntity;
import com.desmond.gadgetstore.entities.CategoryEntity;
import com.desmond.gadgetstore.entities.ProductEntity;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductSpecification {
	
	public static Specification<ProductEntity> getSpecification(ProductListFilterDto filter){
		return (root, query, criteriaBuilder) -> {
			
			List<Predicate> predicates = new ArrayList<>();
			
			Join<ProductEntity, CategoryEntity> categoryJoin = root.join("category");
			Join<ProductEntity, BrandEntity> brandJoin = root.join("brand");

			
			if(StringUtil.notNullNorEmpty(filter.getProductName())) {
				predicates.add(criteriaBuilder.equal(
						root.get("name"), filter.getProductName()));
			}
			
			if(filter.getBrandId() != null) {
				predicates.add(criteriaBuilder.equal(
					brandJoin.get("id"), filter.getBrandId()
				));
			}
			
			if(filter.getCategoryId() != null) {
				predicates.add(criteriaBuilder.equal(
					categoryJoin.get("id"), filter.getCategoryId()
				));
			}
			
			if(filter.getSearchTerm() != null) {
				predicates.add(criteriaBuilder.equal(
						root.get("search_vector @@ to_tsquery(?1)"), filter.getSearchTerm()));
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}

}
