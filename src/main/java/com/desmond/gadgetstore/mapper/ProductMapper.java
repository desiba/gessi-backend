package com.desmond.gadgetstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.payload.response.ProductResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	ProductEntity toDto(ProductResponse product);
}
