package org.slackyogi.model;

import java.util.Map;
import java.util.TreeMap;

public class Basket {
    private static Map<Product, Integer> basketItems;

    public Basket() {
        basketItems = new TreeMap<>();
    }

    public void addItem(Product product, int quantity) {
        basketItems.put(product, quantity);
    }

    public void removeItem(Product product, int quantity) {

    }

    public Map<Product, Integer> getItemsInBasket() {
        return basketItems;
    }
}
