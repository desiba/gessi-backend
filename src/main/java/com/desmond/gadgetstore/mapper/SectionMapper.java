package com.desmond.gadgetstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.desmond.gadgetstore.entities.SectionEntity;
import com.desmond.gadgetstore.payload.response.SectionResponse;

@Mapper(componentModel = "spring")
public interface SectionMapper {
	SectionMapper INSTANCE = Mappers.getMapper(SectionMapper.class);
		
	SectionResponse toResponse(SectionEntity section);
}
