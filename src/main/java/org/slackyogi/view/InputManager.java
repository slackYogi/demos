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
            Optional<Double> doubleCandidate;

            System.out.println(CREATING_PRODUCT_NAME);
            String name = InputManager.getStringInput();

            System.out.println(CREATING_PRODUCT_PRICE);
            doubleCandidate = InputManager.getDoubleInput();
            if (doubleCandidate.isPresent()) {
                price = doubleCandidate.get();
            } else {
                return Optional.empty();
            }

            System.out.println(CREATING_FOOD_MASS);
            doubleCandidate = InputManager.getDoubleInput();
            if (doubleCandidate.isPresent()) {
                mass = doubleCandidate.get();
            } else {
                return Optional.empty();
            }

            if (!name.isBlank() && price > 0 && mass > 0)
                return Optional.of(new Food(name, price, mass));
            return Optional.empty();
        }

        static Optional<Drink> createDrinkFromInput() {
            double price = 0;
            double capacity = 0;
            Optional<Double> doubleCandidate;

            System.out.println(CREATING_PRODUCT_NAME);
            String name = InputManager.getStringInput();

            System.out.println(CREATING_PRODUCT_PRICE);
            doubleCandidate = InputManager.getDoubleInput();
            if (doubleCandidate.isPresent()) {
                price = doubleCandidate.get();
            } else {
                return Optional.empty();
            }

            System.out.println(CREATING_DRINK_CAPACITY);
            doubleCandidate = InputManager.getDoubleInput();
            if (doubleCandidate.isPresent()) {
                capacity = doubleCandidate.get();
            } else {
                return Optional.empty();
            }

            if (!name.isBlank() && price > 0 && capacity > 0)
                return Optional.of(new Drink(name, price, capacity));
            return Optional.empty();
        }

        static Optional<OrderItem> createOrderItemFromInput() {
            Optional<Integer> possibleInt;
            int quantity;

            System.out.println(ADDING_TO_ORDER_PRODUCT_NAME);
            String name = InputManager.getStringInput();

            System.out.println(ADDING_TO_ORDER_PRODUCT_QUANTITY);
            possibleInt = InputManager.getIntInput();
            if (possibleInt.isPresent()) {
                quantity = possibleInt.get();
            } else {
                return Optional.empty();
            }

            if (!name.isBlank() && quantity > 0)
                return Optional.of(new OrderItem(name, quantity));
            return Optional.empty();
        }
    }
}
