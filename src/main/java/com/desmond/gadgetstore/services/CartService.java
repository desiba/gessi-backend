package com.desmond.gadgetstore.services;

import com.desmond.gadgetstore.entities.CartEntity;
import com.desmond.gadgetstore.payload.request.AddCartItemRequest;

import java.util.UUID;

public interface CartService {
    CartEntity add(AddCartItemRequest request);

    CartEntity findUserCart();

    void removeItem(UUID itemId);

    void clear();

}
