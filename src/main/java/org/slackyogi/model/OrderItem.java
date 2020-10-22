package org.slackyogi.model;

import java.util.Objects;

public class OrderItem {
    private String productsName;
    private int quantity;

    public OrderItem(String productsName, int quantity) {
        this.productsName = productsName;
        this.quantity = quantity;
    }

    public String getProductsName() {
        return productsName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return productsName.equals(orderItem.productsName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productsName);
    }

    @Override
    public String toString() {
        return productsName + " in quantity of " + quantity ;
    }
}
