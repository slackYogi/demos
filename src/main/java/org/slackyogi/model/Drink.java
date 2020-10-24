package org.slackyogi.model;

import org.slackyogi.model.enums.ProductType;

public class Drink extends Product {
    double capacity;

    public Drink(ProductType type) {
        super(type);
    }

    public Drink(String name, double price) {
        super(name, price, ProductType.DRINK);
    }

    public Drink(int id, String name, double price) {
        super(id, name, price, ProductType.DRINK);
    }

    public Drink(String name, double price, double capacity) {
        super(name, price, ProductType.DRINK);
        this.capacity = capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public Drink(int id, String name, double price, double capacity) {
        super(id, name, price, ProductType.DRINK);
        this.capacity = capacity;
    }


}
