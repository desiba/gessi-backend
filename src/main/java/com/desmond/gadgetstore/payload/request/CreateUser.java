package com.desmond.gadgetstore.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUser {

    @NotEmpty(message = "FirstName is required")
    private String firstName;

    @NotEmpty(message = "LastName is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email", flags = { Pattern.Flag.CASE_INSENSITIVE })
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

}
