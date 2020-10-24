package org.slackyogi.model;

import org.slackyogi.model.enums.ProductType;

import java.io.Serializable;
import java.util.Objects;

public abstract class Product implements Comparable<Product>, Serializable {
    int id;
    String name;
    double price;
    ProductType type;

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Product (ProductType productType) {
        this.type = productType;
    }

    public Product(String name, double price, ProductType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public Product(int id, String name, double price, ProductType type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    @Override
    public String toString() {
        return name + "\t\t" + price;
    }

    @Override
    public int compareTo(Product p) {
        return name.compareToIgnoreCase(p.name);
    }

    public ProductType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
