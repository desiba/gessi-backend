package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.services.UserService;
import com.desmond.gadgetstore.payload.request.UpdateUser;
import com.desmond.gadgetstore.payload.response.ApiResponse;
import com.desmond.gadgetstore.payload.response.ResponseUtil;
import com.desmond.gadgetstore.payload.response.UserResponse;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserEntity>> findAll(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        List<UserEntity> users = userService.findMany();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<UserEntity>> findById(
    		@Parameter(description = "user id", required = true) @Valid @PathVariable("id") UUID userId
    ){
        Optional<UserEntity> user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
    		@Parameter(description = "user id", required = true) @Valid @PathVariable("id") UUID id,
    		@Valid @RequestBody UpdateUser request
    		){
    	UserResponse updatedUser = userService.update(id, request);
    	return ResponseEntity.ok(ResponseUtil.success("User successfully updated", updatedUser, null));
    }
}
