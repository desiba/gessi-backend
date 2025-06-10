package com.desmond.gadgetstore.payload.request;

import jakarta.validation.constraints.Email;

public class ForgotPasswordRequest {
    @Email(message = "Email can not be empty")
    private String email;
}
