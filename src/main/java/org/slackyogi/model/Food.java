package org.slackyogi.model;

import org.slackyogi.model.enums.ProductType;

public class Food extends Product {
    double mass;

    public Food(ProductType productType) {
        super(productType);
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Food(String name, double price) {
        super(name, price, ProductType.FOOD);
    }

    public Food(int id, String name, double price) {
        super(id, name, price, ProductType.FOOD);
    }

    public Food(String name, double price, double mass) {
        super(name, price, ProductType.FOOD);
        this.mass = mass;
    }

    public Food(int id, String name, double price, double mass) {
        super(id, name, price, ProductType.FOOD);
        this.mass = mass;
    }




}
