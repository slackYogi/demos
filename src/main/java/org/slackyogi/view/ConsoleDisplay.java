package org.slackyogi.view;

import org.slackyogi.data.DatabaseManager;
import org.slackyogi.data.ProductRepository;
import org.slackyogi.model.*;
import org.slackyogi.model.enums.ProductType;
import org.slackyogi.view.enums.MenuOption;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Optional;

import static org.slackyogi.view.enums.Message.*;

public class ConsoleDisplay {
    private static boolean isWorking;
    private static Order order = new Order();
    private static boolean userIsAdmin;
    private static ProductRepository productRepository = new ProductRepository(new DatabaseManager());

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
                viewListOfProductsInStore();
                break;
            case CHECK_IF_PRODUCT_EXISTS_IN_STORE:
                System.out.println(ENTER_SEARCHED_PRODUCT_NAME);
                System.out.println((isProductAvailable())
                        ? SEARCHED_PRODUCT_IS_AVAILABLE
                        : ERROR_NO_SUCH_PRODUCT_AVAILABLE);
                break;
            case ADD_PRODUCT_TO_ORDER:
                addProductToOrder();
                break;
            case VIEW_ORDER_ITEMS:
                viewOrderItems(order.getOrderItems());
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
                        switch (productToBeModified.get().getType()) {

                            case FOOD:
                                Optional<Food> food = InputManager.DataWrapper.createFoodFromInput();
                                if (food.isPresent()) {
                                    Product newProduct = food.get();
                                    productRepository.update(productToBeModified.get(), newProduct);
                                }
                                break;

                            case DRINK:
                                Optional<Drink> drink = InputManager.DataWrapper.createDrinkFromInput();
                                if (drink.isPresent()) {
                                    Product newProduct = drink.get();
                                    productRepository.update(productToBeModified.get(), newProduct);
                                }
                                break;
                        }
                    } else {
                        System.err.println(ERROR_NO_SUCH_PRODUCT_AVAILABLE);
                    }
                }
                break;
            case REMOVE_PRODUCT_FROM_STORE:
                if (userIsAdmin) {
                    tryToDeleteProduct();
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

    private static void tryToDeleteProduct() {
        System.out.println(ENTER_PRODUCT_NAME_FOR_DELETION);
        getProductByName()
                .ifPresentOrElse(product -> productRepository.delete(product),
                        () -> System.out.println(ERROR_NO_SUCH_PRODUCT_AVAILABLE));
    }

    private static boolean isProductAvailable() {
        return productRepository.findByName(InputManager.getStringInput().orElse("")).isPresent();
    }

    private static void addProductToStore() {
        viewTypesForNewProducts();
        System.out.println(ENTER_TYPE_OF_PRODUCT_YOU_WANT_TO_CREATE);

        Optional<ProductType> productType = ProductType
                .fromString(InputManager.getStringInput()
                        .orElse(""));

        if (productType.isPresent()) {
            switch (productType.get()) {
                case FOOD:
                    InputManager.DataWrapper.createFoodFromInput().ifPresentOrElse(product -> productRepository.addProduct(product),
                            () -> System.err.println(ERROR_WRONG_INPUT_PRODUCT_CREATION));
                    break;
                case DRINK:
                    InputManager.DataWrapper.createDrinkFromInput().ifPresentOrElse(product -> productRepository.addProduct(product),
                            () -> System.err.println(ERROR_WRONG_INPUT_PRODUCT_CREATION));
                    break;
                default:
                    System.err.println(ERROR_WRONG_PRODUCT_TYPE);
                    break;
            }
        }
    }

    private static void viewTypesForNewProducts() {
        System.out.println(CREATING_PRODUCT_AVAILABLE_TYPES);
        for (ProductType type : ProductType.values()) {
            System.out.println(type.getDescription());
        }
    }

    private static void addProductToOrder() {
        InputManager.DataWrapper.createOrderItemFromInput()
                .ifPresentOrElse(orderItem -> order.addItem(orderItem),
                        () -> System.err.println(ERROR_NO_SUCH_PRODUCT_AVAILABLE));

        //TODO check if there is enough quantity of product in store
    }

    private static void removeItemFromOrder() {
        InputManager.DataWrapper.createOrderItemFromInput()
                .ifPresentOrElse(orderItem -> order.removeItem(orderItem),
                        () -> System.err.println(ERROR_NO_SUCH_PRODUCT_AVAILABLE));

        // TODO Give proper information if some items remained in basket
    }

    private static Optional<Product> getProductByName() {
        return productRepository.findByName(InputManager.getStringInput().orElse(""));
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

    private static void viewListOfProductsInStore() {
        System.out.println(COLUMNS_OF_PRODUCTS_LISTING);
        productRepository.findAll().forEach(System.out::println);
        //TODO add method for matching column elements
    }

    private static void viewOrderItems(HashSet<OrderItem> orderItems) {
        orderItems.forEach(System.out::println);
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
        productRepository.close();
        isWorking = false;
        InputManager.close();
    }
}
