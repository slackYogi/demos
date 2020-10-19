package org.slackyogi.view;

import org.slackyogi.view.enums.MainMenuOptions;
import org.slackyogi.view.enums.Messages;

public class ConsoleDisplay {
    private static boolean isWorking;

    public static void start() {
        isWorking = true;
        System.out.println(Messages.WELCOMING.getMessage());
        displayLoop();
    }

    private static void displayLoop() {
        while (isWorking) {
            printMenu();
            try {
                actOnUsersChoice(InputManager.getIntInput());
            } catch (IllegalArgumentException ex) {
                System.err.println(Messages.ERROR_NOT_NUMBER);
            }
        }
    }

    private static void actOnUsersChoice(int i) {
        if (i == 0)
            isWorking = false;
    }

    private static void printMenu() {
        System.out.println(Messages.MENU_OPTIONS.getMessage());
        for (MainMenuOptions option: MainMenuOptions.values()) {
            System.out.println(option.getId() + ". " + option.getMessage());
        }
    }




}
