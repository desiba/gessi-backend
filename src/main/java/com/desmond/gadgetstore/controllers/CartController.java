package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.CartEntity;
import com.desmond.gadgetstore.payload.request.AddCartItemRequest;
import com.desmond.gadgetstore.services.CartService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping()
    public ResponseEntity<CartEntity> getCart(){
    	CartEntity userCart = cartService.findUserCart();
        return new ResponseEntity<>(userCart, HttpStatus.OK);
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

    @DeleteMapping("{itemId}")
    public ResponseEntity<CartEntity> deleteItem(
            @Parameter(description = "cart item id", required = true) @Valid @PathVariable("itemId") UUID itemId
    ) {
        cartService.removeItem(itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
