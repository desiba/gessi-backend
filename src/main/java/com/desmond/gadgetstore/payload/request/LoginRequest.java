package com.desmond.gadgetstore.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(name = "email", example="des@gmail.com", required=true)
    private String email;
    @Schema(name = "password", example="P@ss123", required=true)
    private String password;
}
