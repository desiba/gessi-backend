package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.entities.CartEntity;
import com.desmond.gadgetstore.entities.CartItemEntity;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ResourceNotFoundException;
import com.desmond.gadgetstore.payload.request.AddCartItemRequest;
import com.desmond.gadgetstore.repositories.CartItemRepository;
import com.desmond.gadgetstore.repositories.CartRepository;
import com.desmond.gadgetstore.services.CartService;
import com.desmond.gadgetstore.services.ProductService;
import com.desmond.gadgetstore.services.UserService;

import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;
    
    /*
     * 
     * @DeleteMapping
public ResponseEntity deleteAsset(@RequestHeader("Name-Content") String name) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Optional<ClientModel> client = repository.findByEmail(authentication.getPrincipal().toString());
 
    if (client.isEmpty())
        return ResponseMessages.Client_FindByLogin_Null;
 
    //Permission Check
    if(!PermissionManager.permissionCheck("asset-delete", client.get()))
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para executar essa ação.");
 
    Optional<Asset> optional = getAssetByName(client.get(), name);
    if (optional.isPresent()) {
        Asset asset = optional.get();
        try {
            Optional<Organization> organizationOptional = organizationRepository.findByName(client.get().getOrganization().getName());
            if(organizationOptional.isPresent()) {
                Organization organization = organizationOptional.get();
 
                asset.setOrganization(null);
                assetRepository.saveAndFlush(asset);
            }
        } catch (Exception e) {
            BrasensRest.printNicerStackTrace(e);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
 
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
     */

    @Override
    public CartEntity add(AddCartItemRequest request) {
    	List<CartItemEntity> cartItems = new ArrayList<>();
        ProductEntity product = productService.findById(request.getProductId());
    	CartEntity cart = findUserCart();
    	
    	if(cart == null) {
    		cart = CartEntity.builder()
        			.user(userService.loggedIn())
        			.items(new HashSet<>())
        			.build();
    	}
    	
    	cartItems.addAll(cart.getItems());
    	
    	CartItemEntity cartItem = CartItemEntity.builder()
    			.product(product)
    			.quantity(request.getQuantity())
    			.build();
    	
    	cartItems.add(cartItem);
    	
    	cart.getItems().addAll(cartItems);
    	
        return cartRepository.save(cart);
    }

    @Override
    public CartEntity findUserCart() {
        UserEntity user = userService.loggedIn();
        return cartRepository.findByUserId(user.getId());
    }

    @Override
    public void removeItem(UUID itemId) {
    	CartEntity cart = findUserCart();
    	
    	Optional<CartItemEntity> cartItem = cartItemRepository.findById(itemId);
    	    	
    	if(cartItem.isEmpty()) throw new ResourceNotFoundException("cart item not found");
    	
    	boolean isExist = cart.getItems()
    			.stream()
    			.filter(item -> item.getId() == cartItem.get().getId())
    			.findAny()
    			.isPresent();
    	
    	if(!isExist) throw new ResourceNotFoundException("item not found in cart items");
    	
    	// NOT WORKING YET
        cartItemRepository.delete(cartItem.get());
    }

    @Override
    public void clear() {
    	CartEntity cart = findUserCart();
    	cartRepository.delete(cart);
    }
    
    private double calculatePrice(List<CartItemEntity> items) {
    	double totalPrice = 0;
        for (CartItemEntity item : items) {
            //totalPrice += item.ge;	
        }
        return totalPrice;
    }

}
