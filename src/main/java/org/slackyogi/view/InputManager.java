package org.slackyogi.view;

import org.slackyogi.model.Drink;
import org.slackyogi.model.Food;
import org.slackyogi.model.OrderItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Optional;

import static org.slackyogi.view.enums.Message.*;

public class InputManager {

    public static String getStringInput() {
        String input = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (input.isBlank()) {
                input = reader.readLine().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public static Optional<Integer> getIntInput() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return Optional.of(Integer.parseInt(reader.readLine().trim()));
        } catch (NumberFormatException | IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Double> getDoubleInput() throws InputMismatchException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return Optional.of(Double.parseDouble(reader.readLine().trim()));
        } catch (NumberFormatException | IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    static class DataWrapper {

        static Optional<Food> createFoodFromInput() {
            double price = 0;
            double mass = 0;
            System.out.println(CREATING_PRODUCT_NAME);
            String name = InputManager.getStringInput();

            System.out.println(CREATING_PRODUCT_PRICE);
            try {
                price = InputManager.getDoubleInput().orElse(0.0);
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }

            System.out.println(CREATING_FOOD_MASS);
            try {
                mass = InputManager.getDoubleInput().orElse(0.0);
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
            String name = InputManager.getStringInput();

            System.out.println(CREATING_PRODUCT_PRICE);
            try {
                price = InputManager.getDoubleInput().orElse(0.0);
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }

            System.out.println(CREATING_DRINK_CAPACITY);
            try {
                capacity = InputManager.getDoubleInput().orElse(0.0);
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
            }

            if (!name.isBlank() && price > 0 && capacity > 0)
                return Optional.of(new Drink(name, price, capacity));
            return Optional.empty();
        }

        static Optional<OrderItem> createOrderItemFromInput() {
            System.out.println(ADDING_TO_ORDER_PRODUCT_NAME);
            String name = InputManager.getStringInput();

            System.out.println(ADDING_TO_ORDER_PRODUCT_QUANTITY);
            int quantity = InputManager.getIntInput().orElse(0);

            if (!name.isBlank() && quantity > 0)
                return Optional.of(new OrderItem(name, quantity));
            return Optional.empty();
        }
    }
}
