package org.slackyogi.view.enums;

public enum Messages {
    WELCOMING("Welcome!"),
    MENU_OPTIONS("Choose option:"),
    ERROR_NOT_NUMBER("You did not enter a number."),
    ERROR_ENTER_NUMBER_FROM_RANGE("Please enter a number from range of available options.");


    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
