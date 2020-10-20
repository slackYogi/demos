package org.slackyogi.view.enums;

import java.util.Optional;

public enum MainMenuOption {
    VIEW_LIST_OF_ALL_PRODUCTS(1, "View list of all products.", false),
    PRODUCT_EXISTS_IN_STORE(2, "To check if product is in the store.", false),
    ADD_PRODUCT_TO_BASKET(3, "Add product to basket.", false),
    VIEW_BASKET(4, "Show items in basket.", false),
    REMOVE_ITEM_FROM_BASKET(5, "Remove item from basket.", false),
    ADD_PRODUCT_TO_STORE(6, "Add product to store.", true),
    EDIT_PRODUCT_IN_STORE(7, "Edit products data in store.", true),
    REMOVE_PRODUCT_FROM_STORE(8, "Remove product from store.", true),
    RELOG(9, "To log in again.", false),
    EXIT(0, "Quit", false);

    public boolean requiresAdmin() {
        return requiresAdmin;
    }

    private int id;
    private String message;
    private boolean requiresAdmin;

    MainMenuOption(int id, String message, boolean admin) {
        this.id = id;
        this.message = message;
        this.requiresAdmin = admin;
    }

    public static Optional<MainMenuOption> fromNumber(int intInput) {
        for (MainMenuOption option : MainMenuOption.values())
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
