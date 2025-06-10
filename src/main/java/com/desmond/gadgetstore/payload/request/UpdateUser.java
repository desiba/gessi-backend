package com.desmond.gadgetstore.payload.request;

import lombok.Data;

@Data
public class UpdateUser extends CreateUser {
    private String userId;
}
