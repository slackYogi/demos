package org.slackyogi.view.enums;

import java.util.Optional;

public enum MainMenuOptions {
    VIEW_LIST_OF_ALL_PRODUCTS(1, "View list of all products."),
    ADD_PRODUCT_TO_BASKET(2, "Add product to basket."),
    SHOW_BASKET(3, "Show items in basked."),
    EXIT(0, "Quit");

    private int id;
    private String message;

    MainMenuOptions(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public static Optional<MainMenuOptions> fromNumber(int intInput) {
        for (MainMenuOptions option : MainMenuOptions.values())
            if (option.id == intInput) {
                return Optional.of(option);
            }
        return Optional.empty();
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
