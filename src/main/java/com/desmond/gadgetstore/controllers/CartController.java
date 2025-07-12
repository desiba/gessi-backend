package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.CartEntity;
import com.desmond.gadgetstore.payload.request.AddCartItemRequest;
import com.desmond.gadgetstore.payload.response.ApiResponse;
import com.desmond.gadgetstore.payload.response.ResponseUtil;
import com.desmond.gadgetstore.services.CartService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
   
    @GetMapping()
    public ResponseEntity<ApiResponse<CartEntity>> getCart(){
    	CartEntity userCart = cartService.findUserCart();
    	return ResponseEntity.ok(ResponseUtil.success("Successfully retrived cart", userCart, null));
    }

    @PostMapping("addItem")
    public ResponseEntity<CartEntity> addItem(@Valid @RequestBody AddCartItemRequest request) {
        CartEntity itemAdded = cartService.add(request);
        return new ResponseEntity<>(itemAdded, HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<CartEntity> delete() {
        cartService.clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<CartEntity>> deleteItem(
            @Parameter(description = "cart item id", required = true) @Valid @PathVariable("id") UUID itemId
    ) {
        CartEntity cart = cartService.removeItem(itemId);
        
    	return ResponseEntity.ok(ResponseUtil.success("Successfully removed cart item", cart, null));

    }

}
