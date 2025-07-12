package com.desmond.gadgetstore.services.impl;

import com.desmond.gadgetstore.entities.CartEntity;
import com.desmond.gadgetstore.entities.CartItemEntity;
import com.desmond.gadgetstore.entities.ProductEntity;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.exceptions.ConstraintViolationException;
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
        ProductEntity product = productService.findById(request.getProductId());
        CartEntity cart = create();
        
        CartItemEntity cartItem = null;
        
        Optional<CartItemEntity> cartItemExist = cart.getItems()
    			.stream()
    			.filter(item -> item.getProduct().getId().equals(request.getProductId()))
    			.findAny();
        
        if(cartItemExist.isPresent()) {
        	 cartItem = cartItemExist.get();
        	 cartItem.setQuantity(Math.addExact(cartItem.getQuantity(), 1));
        } else {
        	  cartItem = CartItemEntity.builder()
     				.cartId(cart.getId())
         			.product(product)
         			.quantity(request.getQuantity())
         			.build();
        }
        
        cartItemRepository.save(cartItem);
        return cartRepository.findByUserId(cart.getUser().getId());
    }

    @Override
    public CartEntity findUserCart() {
        UserEntity user = userService.getUserLoggedInfo();
        
        return cartRepository.findByUserId(user.getId());
    }

    @Override
    public CartEntity removeItem(UUID itemId) {
    	CartEntity cart = findUserCart();
    	
    	Optional<CartItemEntity> cartItem = cartItemRepository.findByIdAndCartId(itemId, cart.getId());
    	    	
    	if(cartItem.isEmpty()) throw new ResourceNotFoundException("cart item not found");
    	
    	cartItemRepository.delete(cartItem.get());
    	
        return cartRepository.findByUserId(cart.getUser().getId());
    }

    @Override
    public void clear() {
    	CartEntity cart = findUserCart();
    	cartRepository.delete(cart);
    }
    
    private CartEntity create() {
    	CartEntity cart = findUserCart();
    	UserEntity user = userService.getUserLoggedInfo();
    	if(cart == null) {
    		cart = CartEntity.builder().user(user).build();
    		cartRepository.save(cart);
    		cart = cartRepository.findByUserId(user.getId());
    	}
    	return cart;
    }
    
    private double calculatePrice(List<CartItemEntity> items) {
    	double totalPrice = 0;
        for (CartItemEntity item : items) {
            	
        }
        return totalPrice;
    }

}
