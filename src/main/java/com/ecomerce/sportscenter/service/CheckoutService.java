package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.*;
import com.ecomerce.sportscenter.model.CheckoutRequest;
import com.ecomerce.sportscenter.model.OrderItemRequest;
import com.ecomerce.sportscenter.repository.BasketRepository;
import com.ecomerce.sportscenter.repository.OrderRepository;
import com.ecomerce.sportscenter.repository.ProductRepository;
import com.ecomerce.sportscenter.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CheckoutService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductService productService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private long toCents(Long priceInDollars) {
        return priceInDollars * 100;
    }


    public String createCheckoutSession(Long userId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

//        Basket basket = StreamSupport.stream(basketRepository.findAll().spliterator(), false)
//                .filter(b -> userId.equals(b.getUserId()))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));
        Basket basket = basketRepository.findByUserId(userId);

        for (BasketItem basketItem : basket.getItems()) {
            System.out.println("basketItem " + basketItem.getId() + " " + basketItem.getQuantity());
        }

        if (basket.getItems() == null || basket.getItems().isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        List<SessionCreateParams.LineItem> stripeLineItems = new ArrayList<>();
        long totalItemsAmount = 0L;

        for (BasketItem basketItem : basket.getItems()) {
            // Build Stripe line item directly
            stripeLineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(basketItem.getQuantity().longValue())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount(basketItem.getPrice() * 100) // ⚡ ONLY multiply by 100
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(basketItem.getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );

            // Calculate total
            totalItemsAmount += basketItem.getPrice() * basketItem.getQuantity();

            // (Optional) You can still create OrderItems linked to Products if needed

            Product product = productRepository.findById(basketItem.getId())
                    .orElse(null); // if not found, skip decrement
            System.out.println("basket item id : " + basketItem.getId());

            System.out.println("product related to baske item : " + product.getId());

            if (product != null) {
                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(basketItem.getQuantity())
                        .order(null)
                        .build();
                orderItems.add(orderItem);
            }
        }

        // Add Shipping
        if (basket.getShippingPrice() != null && basket.getShippingPrice() > 0) {
            stripeLineItems.add(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount(basket.getShippingPrice() * 100)
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Shipping Cost")
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        long totalAmount = totalItemsAmount + (basket.getShippingPrice() != null ? basket.getShippingPrice() * 100 : 0L);

        Order tempOrder = Order.builder()
                .user(user)
                .deliveryStatus(DeliveryStatus.PENDING)
                .orderItems(orderItems)
                .totalAmount(totalAmount)
                .shippingPrice(basket.getShippingPrice() != null ? basket.getShippingPrice() * 100 : 0L)
                .build();

        orderItems.forEach(orderItem -> {
            System.out.println("orderItem " + orderItem.getQuantity());
            orderItem.setOrder(tempOrder);
        });

        Order order = orderRepository.save(tempOrder);

        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success")
                .setCancelUrl("http://localhost:4200/cancel");

        for (SessionCreateParams.LineItem item : stripeLineItems) {
            sessionBuilder.addLineItem(item);
        }

        Session session = Session.create(sessionBuilder.build());

        order.setStripeSessionId(session.getId());
        orderRepository.save(order);

        for (OrderItem item : orderItems) {
            if (item.getProduct() != null) {
                productService.decrementQuantity(item.getProduct().getId(), item.getQuantity());
            }
        }

        return session.getUrl();
    }


}
