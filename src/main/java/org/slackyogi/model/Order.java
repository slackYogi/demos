package org.slackyogi.model;

import org.slackyogi.view.enums.Message;

import java.util.HashSet;

public class Order {
    private static HashSet<OrderItem> orderItems;

    public Order() {
        orderItems = new HashSet<>();
    }

    public void addItem(OrderItem orderItem) {
        if (!orderItems.add(orderItem)) {
            increaseQuantityOfExistingItem(orderItem);
        }
    }

    public void removeItem(OrderItem orderItemForRemoval) {
        orderItems.stream()
                .filter(orderItem -> orderItem.getProductsName().equals(orderItemForRemoval.getProductsName()))
                .findFirst()
                .ifPresentOrElse(orderItem -> {
                            if (productQuantityTooLow(orderItem, orderItemForRemoval)) {
                                orderItems.remove(orderItem);
                            } else {
                                orderItem.setQuantity(orderItem.getQuantity() - orderItemForRemoval.getQuantity());
                            }
                        },
                        () -> System.err.println(Message.ERROR_NO_SUCH_PRODUCT_IN_ORDER));
    }

    public HashSet<OrderItem> getOrderItems() {
        return orderItems;
    }

    private static void increaseQuantityOfExistingItem(OrderItem modifyingOrderItem) {
        orderItems.stream()
                .filter(orderItem -> orderItem.getProductsName().equals(modifyingOrderItem.getProductsName()))
                .findFirst()
                .ifPresentOrElse(orderItem -> orderItem.setQuantity(orderItem.getQuantity() + modifyingOrderItem.getQuantity()),
                        () -> System.err.println(Message.ERROR_NO_SUCH_PRODUCT_IN_ORDER));

    }

    private static boolean productQuantityTooLow(OrderItem toModify, OrderItem modifying) {
        return modifying.getQuantity() >= toModify.getQuantity();
    }
}

