package com.desmond.gadgetstore.payload.response;

import java.util.Set;

import com.desmond.gadgetstore.entities.RoleEntity;

public record LoginResponse(
      boolean isLogged,
      Set<RoleEntity> roles
) {
}
