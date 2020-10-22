package org.slackyogi.model;

import org.slackyogi.model.enums.ProductType;

public class Food extends Product {
    double mass;

    public Food(String name, double price) {
        super(name, price, ProductType.FOOD);
    }

    public Food(String name, double price, double mass) {
        super(name, price, ProductType.FOOD);

        this.mass = mass;
    }




}
