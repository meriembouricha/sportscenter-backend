package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Basket;
import com.ecomerce.sportscenter.model.BasketResponse;

import java.util.List;

public interface BasketService {
    List<BasketResponse> getAllBaskets(Long userId);
    BasketResponse getBasketById(String basketId, Long userId);
    void deleteBasketById(String basketId, Long userId);
    BasketResponse createBasket(Basket basket);
    BasketResponse updateShippingPrice(String basketId, Long userId, Long shippingPrice);
}
