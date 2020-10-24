package org.slackyogi.view.enums;

import java.util.Optional;

public enum MenuOption {
    VIEW_LIST_OF_ALL_PRODUCTS(1, "View list of all products.", false),
    CHECK_IF_PRODUCT_EXISTS_IN_STORE(2, "To check if product is in the store.", false),
    ADD_PRODUCT_TO_ORDER(3, "Add product to order.", false),
    VIEW_ORDER_ITEMS(4, "Show items in order.", false),
    REMOVE_ITEM_FROM_ORDER(5, "Remove item from order.", false),
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

    MenuOption(int id, String message, boolean admin) {
        this.id = id;
        this.message = message;
        this.requiresAdmin = admin;
    }

    public static Optional<MenuOption> fromNumber(int intInput) {
        for (MenuOption option : MenuOption.values())
            if (option.id == intInput) {
                return Optional.of(option);
            }
        return Optional.empty();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return id + ". " + getMessage();
    }
}
