package com.desmond.gadgetstore.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(name = "email", example="owner@gmail.com")
    @NotBlank
    @Size(min = 3, max = 20)
    @Email
    private String email;
    
    @Schema(name = "password", example="P@ss123")
    @NotBlank
    @Size(max = 50)
    private String password;
}
