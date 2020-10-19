package org.slackyogi.view;

import java.util.Scanner;

public class InputManager {
    private static Scanner scanner = new Scanner(System.in);

    public static int getIntInput() throws IllegalArgumentException {
        int input;

        if (scanner.hasNextInt()) {
            input = scanner.nextInt();
            return input;
        }
        else {
            scanner.nextLine();
            throw new IllegalArgumentException();
        }
    }

}
