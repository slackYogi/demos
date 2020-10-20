package org.slackyogi.view;

import org.slackyogi.data.ProductRepository;
import org.slackyogi.model.*;
import org.slackyogi.model.enums.ProductType;
import org.slackyogi.view.enums.MenuOption;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Optional;

import static org.slackyogi.view.enums.Message.*;

public class ConsoleDisplay {
    private static boolean isWorking;
    private static ProductRepository productRepository = new ProductRepository();
    private static Order order = new Order();
    private static boolean userIsAdmin;

    public static void start() {
        isWorking = true;
        loggingIn();
        displayLoop();
    }

    private static void displayLoop() {
        while (isWorking) {
            printMenu();
            getUsersChoice();
        }
    }

    private static void getUsersChoice() {
        try {
            MenuOption.fromNumber(InputManager.getIntInput())
                    .ifPresentOrElse(option -> actOnUsersChoice(option),
                            () -> System.err.println(ERROR_ENTER_NUMBER_FROM_RANGE));
        } catch (InputMismatchException ex) {
            System.err.println(ERROR_NOT_NUMBER);
        }
    }

    private static void actOnUsersChoice(MenuOption option) { //TODO Instead of switch create abstract class Page and Pages for each switch case?
        switch (option) {
            case VIEW_LIST_OF_ALL_PRODUCTS:
                viewListOfAllProducts();
                break;
            case PRODUCT_EXISTS_IN_STORE:
                System.out.println(ENTER_SEARCHED_PRODUCT_NAME);
                System.out.println((isProductAvailable())
                        ? SEARCHED_PRODUCT_IS_AVAILABLE
                        : ERROR_NO_SUCH_PRODUCT_AVAILABLE);
                break;
            case ADD_PRODUCT_TO_ORDER:
                addProductToOrder();
                break;
            case VIEW_ORDER:
                viewOrderItems(order.getItemsInBasket());
                break;
            case REMOVE_ITEM_FROM_ORDER:
                removeItemFromOrder();
                break;
            case ADD_PRODUCT_TO_STORE:
                if (userIsAdmin) {
                    addProductToStore();
                }
                break;
            case EDIT_PRODUCT_IN_STORE:
                if (userIsAdmin) {
                    System.out.println(ENTER_PRODUCT_NAME_TO_BE_UPDATED);
                    Optional<Product> productToBeModified = getProductByName();
                    if (productToBeModified.isPresent()) {
                        if (productToBeModified.get() instanceof Food) {
                            Optional<Food> food = getFoodDataFromUser();
                            if (food.isPresent()) {
                                Product newProduct = food.get();
                                productRepository.update(productToBeModified.get(), newProduct);
                            }
                        } else if (productToBeModified.get() instanceof Drink) {
                            Optional<Drink> drink = getDrinkDataFromUser();                                 //TODO eliminate this repetition
                            if (drink.isPresent()) {
                                Product newProduct = drink.get();
                                productRepository.update(productToBeModified.get(), newProduct);
                            }
                        } else {
                            System.err.println(ERROR_NO_SUCH_PRODUCT_AVAILABLE);
                        }
                    }
                }
                break;
            case REMOVE_PRODUCT_FROM_STORE:
                if (userIsAdmin) {
                    System.out.println(ENTER_PRODUCT_NAME_FOR_DELETION);
                    tryToDeleteProduct(productRepository.findByName(InputManager.getStringInput().orElse("")));
                }
                break;
            case RELOG:
                userIsAdmin = false;
                start();
                break;
            case EXIT:
                onExit();
                break;
            default:
                break;
        }
    }

    private static void tryToDeleteProduct(Optional<Product> productToBeDeleted) {
        if (productToBeDeleted.isPresent()) {
            productRepository.delete(productToBeDeleted.get());
        } else {
            System.out.println(ERROR_NO_SUCH_PRODUCT_AVAILABLE);
        }
    }

    private static boolean isProductAvailable() {
        return productRepository.findByName(InputManager.getStringInput().orElse("")).isPresent();
    }

    private static void addProductToStore() {
        viewTypesForNewProducts();
        System.out.println(CREATING_PRODUCT_OF_A_TYPE);

        Optional<ProductType> optionalType = ProductType
                .fromString(InputManager.getStringInput()
                        .orElse(""));

        if (optionalType.isPresent()) {
            switch (optionalType.get()) {
                case FOOD:
                    Optional<Food> foodProduct = getFoodDataFromUser();
                    if (foodProduct.isPresent()) {
                        productRepository.addProduct(foodProduct.get());
                    } else {
                        System.err.println(ERROR_WRONG_INPUT_PRODUCT_CREATION);
                    }
                    break;
                case DRINK:
                    Optional<Drink> drinkProduct = getDrinkDataFromUser();
                    if (drinkProduct.isPresent()) {
                        productRepository.addProduct(drinkProduct.get());
                    } else {
                        System.err.println(ERROR_WRONG_INPUT_PRODUCT_CREATION);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static Optional<Food> getFoodDataFromUser() {       //TODO some generic method?
        System.out.println(CREATING_PRODUCT_NAME); //TODO change entering values as one string?
        String name = InputManager.getStringInput().orElse("");

        System.out.println(CREATING_PRODUCT_PRICE);
        double price = InputManager.getDoubleInput();

        System.out.println(CREATING_FOOD_MASS);
        double mass = InputManager.getDoubleInput();

        if (!name.isBlank() && price > 0 && mass > 0)
            return Optional.of(new Food(name, price, mass));
        return Optional.empty();
    }

    private static Optional<Drink> getDrinkDataFromUser() {
        System.out.println(CREATING_PRODUCT_NAME);
        String name = InputManager.getStringInput().orElse("");

        System.out.println(CREATING_PRODUCT_PRICE);
        double price = InputManager.getDoubleInput();

        System.out.println(CREATING_DRINK_CAPACITY);
        double capacity = InputManager.getDoubleInput();

        if (!name.isBlank() && price > 0 && capacity > 0)
            return Optional.of(new Drink(name, price, capacity));
        return Optional.empty();
    }

    private static void viewTypesForNewProducts() {
        System.out.println(CREATING_PRODUCT_AVAILABLE_TYPES);
        for (ProductType type : ProductType.values()) {
            System.out.println(type.getDescription());
        }
    }

    private static void removeItemFromOrder() {
        OrderItem orderItem = getOrderItemData();            //TODO try to remove model information from view
        if (orderItemHasCorrectData(orderItem)) {
            productRepository.findByName(orderItem.getName())
                    .ifPresentOrElse(product -> order.removeItem(product, orderItem.getQuantity()),
                            () -> System.err.println(ERROR_NO_SUCH_ITEM_IN_STORE + orderItem.getName()));

            System.out.println("Removed " + orderItem.getQuantity() + " " + orderItem.getName() + " from order."); //TODO Give proper information if some items remained in basker
        }
    }

    private static void addProductToOrder() {
        OrderItem orderItem = getOrderItemData();
        if (orderItemHasCorrectData(orderItem)) {
            productRepository.findByName(orderItem.getName())
                    .ifPresentOrElse(product -> order.addItem(product, orderItem.getQuantity()),
                            () -> System.err.println(ERROR_NO_SUCH_ITEM_IN_STORE + orderItem.getName()));
            //TODO check if there is enough quantity of product
            System.out.println("Added " + orderItem.getQuantity() + " " + orderItem.getName() + " to order."); //TODO add 's' for plural items
        }
    }

    private static boolean orderItemHasCorrectData(OrderItem orderItem) {
        if (orderItem.getName().trim().isBlank()) {
            System.err.println(ERROR_MODIFYING_ORDER_WRONG_NAME);
            return false;
        } else if (orderItem.getQuantity() <= 0) {
            System.err.println(ERROR_ADDING_TO_ORDER_WRONG_QUANTITY);
            return false;
        } else {
            return true;
        }
    }

    private static Optional<Product> getProductByName() {
        return productRepository.findByName(InputManager.getStringInput().orElse(""));
    }

    private static OrderItem getOrderItemData() {
        System.out.println(ADDING_TO_ORDER_PRODUCT_NAME);
        String name = InputManager.getStringInput().orElse("");

        System.out.println(ADDING_TO_ORDER_PRODUCT_QUANTITY);
        int quantity = InputManager.getIntInput();

        return new OrderItem(name, quantity);
    }

    private static void printMenu() {
        System.out.println(MENU_OPTIONS);
        for (MenuOption option : MenuOption.values()) {
            if (!option.requiresAdmin())
                System.out.println(option.toString());
            else {
                if (userIsAdmin) {
                    System.out.println(option.toString());
                }
            }
        }
    }

    private static void viewListOfAllProducts() {
        System.out.println(COLUMNS_OF_PRODUCTS_LISTING);
        for (Product product : productRepository.findAll()) {
            System.out.println(product);                                    //TODO add method for matching column elements
        }

    }

    private static void viewOrderItems(Map<Product, Integer> orderItems) {
        for (Product product : orderItems.keySet()) {
            System.out.println("Item: " + product.getName() + " in " + orderItems.get(product) + " quantity.");
        }
    }

    private static void loggingIn() {
        System.out.println(LOGGING);
        String login = InputManager.getStringInput().orElse("guest");
        if (login.equals("admin")) {
            System.out.println(LOGGED_EMPLOYEE);
            userIsAdmin = true;
        } else {
            System.out.println(LOGGED_CLIENT);
        }
        System.out.println(WELCOMING + login + "!");
    }

    private static void onExit() {
        productRepository.saveDatabase();
        isWorking = false;
        InputManager.close();
    }
}
