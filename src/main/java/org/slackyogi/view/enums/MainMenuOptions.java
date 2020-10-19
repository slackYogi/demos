package org.slackyogi.view.enums;

public enum MainMenuOptions {
    VIEW_LIST_OF_ALL_PRODUCTS(1, "View list of all products."),
    ADD_PRODUCT_TO_BASKET(2, "Add product to basket."),
    EXIT(0, "Quit");

    private int id;
    private String message;

    MainMenuOptions(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
