package org.slackyogi.model;

import java.util.Map;
import java.util.TreeMap;

public class Order {
    private static Map<Product, Integer> orderItems;

    public Order() {
        orderItems = new TreeMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (orderItems.containsKey(product)) {
            increaseQuantityOfExistingItem(product, quantity);
        } else {
            orderItems.put(product, quantity);
        }
    }

    public void removeItem(Product product, int quantity) {
        if (orderItems.containsKey(product)) {
            if (orderItems.get(product) - quantity > 0) {
                orderItems.put(product, orderItems.get(product) - quantity);
            } else {
                orderItems.remove(product);
            }
        }
    }

    public Map<Product, Integer> getItemsInBasket() {
        return orderItems;
    }

    private static void increaseQuantityOfExistingItem(Product product, int quantity) {
        orderItems.put(product, orderItems.get(product) + quantity);
    }
}
