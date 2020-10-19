package org.slackyogi.view;

public class ConsoleDisplay {
    private static boolean isWorking;

    public static void start() {
        isWorking = true;
        System.out.println("Welcome!"); //TODO Change to enum Messages
        displayLoop();
    }

    private static void displayLoop() {
        while (isWorking) {
            printMenu();
            try {
                actOnUsersChoice(InputManager.getIntInput());    //TODO add enum for possible options
            } catch (IllegalArgumentException ex) {
                System.err.println("You did not enter a number."); //TODO -> to enum
            }
        }
    }

    private static void actOnUsersChoice(int i) {
        if (i == 0)
            isWorking = false;
    }

    private static void printMenu() {
        System.out.println("Choose option:");
    }




}
