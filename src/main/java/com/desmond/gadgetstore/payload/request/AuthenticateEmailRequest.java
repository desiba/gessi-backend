package com.desmond.gadgetstore.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AuthenticateEmailRequest {
    @NotEmpty(message = "email can not be empty")
    private String email;
    @NotEmpty(message = "token can not be empty")
    private String token;
}
