package org.slackyogi.view;

import org.slackyogi.model.Drink;
import org.slackyogi.model.Food;
import org.slackyogi.model.OrderItem;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import static org.slackyogi.view.enums.Message.*;

public class InputManager {
    private static Scanner scanner = new Scanner(System.in);

    public static Optional<String> getStringInput() {
        if (scanner.hasNext()) {
            return Optional.of(scanner.nextLine());
        } else
            return Optional.empty();
    }

    public static int getIntInput() throws InputMismatchException {
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
            throw new InputMismatchException();
        }
    }

    public static void close() {
        scanner.close();
    }

    public static double getDoubleInput() throws IllegalArgumentException {
        double input;

        if (scanner.hasNextDouble()) {
            input = scanner.nextDouble();
            scanner.nextLine();
            return input;
        } else {
            scanner.nextLine();
            throw new IllegalArgumentException("Wrong input, enter a double number.");
        }
    }

    static class DataWrapper {

        static Optional<Food> createFoodFromInput() {
            double price = 0;
            double mass = 0;
            System.out.println(CREATING_PRODUCT_NAME);
            String name = InputManager.getStringInput().orElse("");

            System.out.println(CREATING_PRODUCT_PRICE);
            try {
                price = InputManager.getDoubleInput();
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }

            System.out.println(CREATING_FOOD_MASS);
            try {
                mass = InputManager.getDoubleInput();
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
            }

            if (!name.isBlank() && price > 0 && mass > 0)
                return Optional.of(new Food(name, price, mass));
            return Optional.empty();
        }

        static Optional<Drink> createDrinkFromInput() {
            double price = 0;
            double capacity = 0;
            System.out.println(CREATING_PRODUCT_NAME);
            String name = InputManager.getStringInput().orElse("");

            System.out.println(CREATING_PRODUCT_PRICE);
            try {
                price = InputManager.getDoubleInput();
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }

            System.out.println(CREATING_DRINK_CAPACITY);
            try {
                capacity = InputManager.getDoubleInput();
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
            }

            if (!name.isBlank() && price > 0 && capacity > 0)
                return Optional.of(new Drink(name, price, capacity));
            return Optional.empty();
        }

        static Optional<OrderItem> createOrderItemFromInput() {
            System.out.println(ADDING_TO_ORDER_PRODUCT_NAME);
            String name = InputManager.getStringInput().orElse("");

            System.out.println(ADDING_TO_ORDER_PRODUCT_QUANTITY);
            int quantity = InputManager.getIntInput();

            if (!name.isBlank() && quantity > 0)
                return Optional.of(new OrderItem(name, quantity));
            return Optional.empty();
        }
    }
}
