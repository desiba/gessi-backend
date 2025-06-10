package com.desmond.gadgetstore.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAddress {
    @NotBlank(message = "The countryId is required.")
    private String countryId;

    @NotBlank(message = "The stateId is required.")
    private String stateId;

    @NotBlank(message = "The cityId is required.")
    private String cityId;

    @NotBlank(message = "The Zip code is required.")
    @Pattern(regexp = "^\\d{1,5}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "The Zip code is invalid.")
    private String zipCode;

    @NotBlank(message = "The street name is required.")
    private String street;
}
