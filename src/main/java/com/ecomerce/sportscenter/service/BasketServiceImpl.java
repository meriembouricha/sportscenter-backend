package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Basket;
import com.ecomerce.sportscenter.entity.BasketItem;
import com.ecomerce.sportscenter.model.BasketItemResponse;
import com.ecomerce.sportscenter.model.BasketResponse;
import com.ecomerce.sportscenter.repository.BasketRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;

    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public List<BasketResponse> getAllBaskets(Long userId) {
        log.info("Fetching all baskets for user {}", userId);
        List<Basket> basketList = ((List<Basket>) basketRepository.findAll()).stream()
                .filter(basket -> userId.equals(basket.getUserId()))
                .collect(Collectors.toList());

        return basketList.stream()
                .map(this::convertToBasketResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BasketResponse getBasketById(String basketId, Long userId) {
        log.info("Fetching Basket by Id: {} for user {}", basketId, userId);
        Optional<Basket> basketOptional = basketRepository.findById(basketId);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            if (!userId.equals(basket.getUserId())) {
                log.warn("User {} tried to access basket {} owned by another user", userId, basketId);
                return null; // or throw an exception (Forbidden)
            }
            return convertToBasketResponse(basket);
        }
        return null;
    }

    @Override
    public void deleteBasketById(String basketId, Long userId) {
        log.info("Deleting Basket by Id: {} for user {}", basketId, userId);
        Optional<Basket> basketOptional = basketRepository.findById(basketId);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            if (userId.equals(basket.getUserId())) {
                basketRepository.deleteById(basketId);
                log.info("Deleted Basket by Id: {}", basketId);
            } else {
                log.warn("User {} tried to delete basket {} owned by another user", userId, basketId);
                // Optionally throw an exception (Forbidden)
            }
        }
    }

    @Override
    public BasketResponse createBasket(Basket basket) {
        log.info("Creating basket for user {}", basket.getUserId());
        Basket savedBasket = basketRepository.save(basket);
        log.info("Basket created with Id: {}", savedBasket.getId());
        return convertToBasketResponse(savedBasket);
    }

    @Override
    public BasketResponse updateShippingPrice(String basketId, Long userId, Long shippingPrice) {
        Optional<Basket> basketOptional = basketRepository.findById(basketId);

        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            if (!userId.equals(basket.getUserId())) {
                log.warn("User {} tried to update shipping price for basket {} owned by another user", userId, basketId);
                return null; // or throw forbidden
            }
            basket.setShippingPrice(shippingPrice);
            Basket savedBasket = basketRepository.save(basket);
            log.info("Updated shipping price for basket {} to {}", basketId, shippingPrice);
            return convertToBasketResponse(savedBasket);
        }
        return null;
    }


    private BasketResponse convertToBasketResponse(Basket basket) {
        if (basket == null) {
            return null;
        }
        List<BasketItemResponse> itemResponses = basket.getItems().stream()
                .map(this::convertToBasketItemResponse)
                .collect(Collectors.toList());
        return BasketResponse.builder()
                .id(basket.getId())
                .items(itemResponses)
                .shippingPrice(basket.getShippingPrice())
                .build();
    }

    private BasketItemResponse convertToBasketItemResponse(BasketItem basketItem) {
        return BasketItemResponse.builder()
                .id(basketItem.getId())
                .name(basketItem.getName())
                .description(basketItem.getDescription())
                .price(basketItem.getPrice())
                .pictureUrl(basketItem.getPictureUrl())
                .productBrand(basketItem.getProductBrand())
                .productType(basketItem.getProductType())
                .quantity(basketItem.getQuantity())
                .build();
    }
}
