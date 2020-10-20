package org.slackyogi.model;

import java.util.Map;
import java.util.TreeMap;

public class Basket {
    private static Map<Product, Integer> basketItems;

    public Basket() {
        basketItems = new TreeMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (basketItems.containsKey(product)) {
            increaseQuantityOfExistingItem(product, quantity);
        } else {
            basketItems.put(product, quantity);
        }
    }

    public void removeItem(Product product, int quantity) {
        if (basketItems.containsKey(product)) {
            if (basketItems.get(product) - quantity > 0) {
                basketItems.put(product, basketItems.get(product) - quantity);
            } else {
                basketItems.remove(product);
            }
        }
    }

    public Map<Product, Integer> getItemsInBasket() {
        return basketItems;
    }

    private static void increaseQuantityOfExistingItem(Product product, int quantity) {
        basketItems.put(product, basketItems.get(product) + quantity);
    }
}
