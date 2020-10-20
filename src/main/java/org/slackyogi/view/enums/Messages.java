package org.slackyogi.view.enums;

public enum Messages {
    WELCOMING("Welcome!"),
    MENU_OPTIONS("Choose option:"),
    COLUMNS_OF_PRODUCTS_LISTING("Product:\t\tPrice:"), //TODO create method in Product class to get this string, making it not magical
    ADDING_TO_BASKET_PRODUCT_NAME("Enter desired product name:"),
    ADDING_TO_BASKET_PRODUCT_QUANTITY("Enter quantity of product:"),
    ERROR_NOT_NUMBER("You did not enter a number."),
    ERROR_ENTER_NUMBER_FROM_RANGE("Please enter a number from range of available options."),
    ERROR_ADDING_TO_BASKET_WRONG_NAME("You have to enter products name."),
    ERROR_ADDING_TO_BASKET_WRONG_QUANTITY("Quantity of products has to be greater than 0."),
    ERROR_NO_SUCH_ITEM_IN_STORE("Store does not have item named: ");


    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
