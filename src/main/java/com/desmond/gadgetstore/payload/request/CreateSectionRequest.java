package com.desmond.gadgetstore.payload.request;

import com.desmond.gadgetstore.common.utils.SectionType;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSectionRequest {
	@NotEmpty(message = "title is required")
	private String title;
	private SectionType type;
}
