package com.desmond.gadgetstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductImageRequest {
    private String url;
    private String name;
    private String description;
    ;
}
