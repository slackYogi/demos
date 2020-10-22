package org.slackyogi.model;

import org.slackyogi.model.enums.ProductType;

public class Drink extends Product {
    double capacity;

    public Drink(String name, double price) {
        super(name, price, ProductType.DRINK);
    }

    public Drink(String name, double price, double capacity) {
        super(name, price, ProductType.DRINK);
        this.capacity = capacity;
    }


}
