package org.slackyogi.model;

public class Drink extends Product {
    double capacity;

    public Drink(String name, double price) {
        super(name, price);
    }

    public Drink(String name, double price, double capacity) {
        super(name, price);
        this.capacity = capacity;
    }


}
