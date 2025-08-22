package com.ecomerce.sportscenter.controller;

import com.ecomerce.sportscenter.Security.JwtHelper;
import com.ecomerce.sportscenter.entity.Basket;
import com.ecomerce.sportscenter.entity.BasketItem;
import com.ecomerce.sportscenter.model.BasketItemResponse;
import com.ecomerce.sportscenter.model.BasketResponse;
import com.ecomerce.sportscenter.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/baskets")
public class BasketController {
    private final BasketService basketService;
    private final JwtHelper jwtHelper;

    public BasketController(BasketService basketService, JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
        this.basketService = basketService;
    }

    @GetMapping
    public List<BasketResponse> getAllBaskets(@RequestHeader("Authorization") String tokenHeader) {
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);
        Long userId = jwtHelper.getUserIdFromToken(token);
        return basketService.getAllBaskets(userId);
    }

    @GetMapping("/{basketId}")
    public ResponseEntity<BasketResponse> getBasketById(@PathVariable String basketId, @RequestHeader("Authorization") String tokenHeader) {
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);
        Long userId = jwtHelper.getUserIdFromToken(token);
        BasketResponse basket = basketService.getBasketById(basketId, userId);
        if (basket != null) {
            return ResponseEntity.ok(basket);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{basketId}")
    public ResponseEntity<Void> deleteBasketById(@PathVariable String basketId, @RequestHeader("Authorization") String tokenHeader) {
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);
        Long userId = jwtHelper.getUserIdFromToken(token);
        basketService.deleteBasketById(basketId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<BasketResponse> createBasket(@RequestBody BasketResponse basketResponse, @RequestHeader("Authorization") String tokenHeader) {
        String token = jwtHelper.extractTokenFromHeader(tokenHeader);
        Long userId = jwtHelper.getUserIdFromToken(token);
        Basket basket = convertToBasketEntity(basketResponse);
        basket.setUserId(userId);
        BasketResponse createdBasket = basketService.createBasket(basket);
        return new ResponseEntity<>(createdBasket, HttpStatus.CREATED);
    }

    @PutMapping("/{basketId}/shipping")
    public ResponseEntity<BasketResponse> updateShippingPrice(
            @PathVariable String basketId,
            @RequestParam Long shippingPrice,
            @RequestHeader("Authorization") String tokenHeader) {

        String token = jwtHelper.extractTokenFromHeader(tokenHeader);
        Long userId = jwtHelper.getUserIdFromToken(token);

        BasketResponse updatedBasket = basketService.updateShippingPrice(basketId, userId, shippingPrice);

        if (updatedBasket != null) {
            return ResponseEntity.ok(updatedBasket);
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }


    private Basket convertToBasketEntity(BasketResponse basketResponse) {
        Basket basket = new Basket();
        basket.setId(basketResponse.getId());
        basket.setItems(mapBasketItemResponsesToEntities(basketResponse.getItems()));
        return basket;
    }

    private List<BasketItem> mapBasketItemResponsesToEntities(List<BasketItemResponse> itemResponses) {
        return itemResponses.stream()
                .map(this::convertToBasketItemEntity)
                .collect(Collectors.toList());
    }

    private BasketItem convertToBasketItemEntity(BasketItemResponse itemResponse) {
        BasketItem basketItem = new BasketItem();
        basketItem.setId(itemResponse.getId());
        basketItem.setName(itemResponse.getName());
        basketItem.setDescription(itemResponse.getDescription());
        basketItem.setPrice(itemResponse.getPrice());
        basketItem.setPictureUrl(itemResponse.getPictureUrl());
        basketItem.setProductBrand(itemResponse.getProductBrand());
        basketItem.setProductType(itemResponse.getProductType());
        basketItem.setQuantity(itemResponse.getQuantity());
        return basketItem;
    }

}
