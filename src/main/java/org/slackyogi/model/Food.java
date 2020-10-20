package org.slackyogi.model;

public class Food extends Product {
    double mass;

    public Food(String name, double price) {
        super(name, price);
    }

    public Food(String name, double price, double mass) {
        super(name, price);
        this.mass = mass;
    }


}
