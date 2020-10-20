package org.slackyogi.view;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class InputManager {
    private static Scanner scanner = new Scanner(System.in);

    public static Optional<String> getStringInput() {
        if (scanner.hasNext()) {
            return Optional.of(scanner.nextLine());
        } else
            return Optional.empty();
    }

    public static int getIntInput() throws IllegalArgumentException { //TODO extract generic method to get number int or double?
        int input;

        if (scanner.hasNextInt()) {
            input = scanner.nextInt();
            try {
                scanner.nextLine();
            } catch (NoSuchElementException ex) {
                // TODO As I modified input stream in testing class this was required. Do it better.
            }
            return input;
        } else {
            try {
                scanner.nextLine();
            } catch (NoSuchElementException ex) {
                // TODO As I modified input stream in testing class this was required. Do it better.
            }
            throw new IllegalArgumentException();
        }
    }



    public static double getDoubleInput() throws IllegalArgumentException {
        double input;

        if (scanner.hasNextDouble()) {
            input = scanner.nextDouble();
                scanner.nextLine();
            return input;
        } else {
                scanner.nextLine();
            throw new IllegalArgumentException();
        }
    }
}
