package com.example.service;

import com.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CheckoutService {

    @Autowired
    private CartService cartService;

    public String checkout() {
        Collection<Product> items = cartService.getCart().getItems().values();
        if (items.isEmpty()) {
            return "Your cart is empty!";
        }
        double total = items.stream()
                .mapToDouble(p -> p.getPrice() != null ? p.getPrice().doubleValue() : 0.0)
                .sum();
        int count = items.size();
        cartService.clearCart();
        return "Checkout successful! " + count + " items purchased. Total: $" + String.format("%.2f", total);
    }
}