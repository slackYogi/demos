package org.slackyogi.view.enums;

public enum Messages {
    WELCOMING("Welcome!"),
    MENU_OPTIONS("Choose option:"),
    ERROR_NOT_NUMBER("You did not enter a number.");


    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
