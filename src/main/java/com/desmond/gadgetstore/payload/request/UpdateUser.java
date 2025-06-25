package com.desmond.gadgetstore.payload.request;

import lombok.Data;

@Data
public class UpdateUser {
    private String firstName;
    private String lastName;
}