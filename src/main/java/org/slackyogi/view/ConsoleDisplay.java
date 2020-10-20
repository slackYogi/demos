package org.slackyogi.view;

import org.slackyogi.data.ProductRepository;
import org.slackyogi.model.Basket;
import org.slackyogi.model.Product;
import org.slackyogi.view.enums.MainMenuOptions;

import java.util.Map;

import static org.slackyogi.view.enums.Messages.*;

public class ConsoleDisplay {
    private static boolean isWorking;
    private static ProductRepository productRepository = new ProductRepository();
    private static Basket basket = new Basket();

    public static void start() {
        isWorking = true;
        System.out.println(WELCOMING.getMessage());
        displayLoop();
    }

    private static void displayLoop() {
        while (isWorking) {
            printMenu();
            try {
                MainMenuOptions
                        .fromNumber(InputManager.getIntInput())
                        .ifPresentOrElse(option -> actOnUsersChoice(option),
                                () -> System.err.println(ERROR_ENTER_NUMBER_FROM_RANGE.getMessage()));
            } catch (IllegalArgumentException ex) {
                System.err.println(ERROR_NOT_NUMBER.getMessage());
            }
        }
    }

    private static void actOnUsersChoice(MainMenuOptions option) {
        switch (option) {
            case VIEW_LIST_OF_ALL_PRODUCTS:
                viewListOfAllProducts();
                break;
            case ADD_PRODUCT_TO_BASKET:
                addProductToBasket();
                break;
            case VIEW_BASKET:
                viewItemsInBasket(basket.getItemsInBasket());
                break;
            case EXIT:
                isWorking = false;
                break;
            default:
                break;
        }
    }

    private static void addProductToBasket() {                      // TODO consider creating Order class
        System.out.println(ADDING_TO_BASKET_PRODUCT_NAME.getMessage());
        String name = InputManager.getStringInput().orElse("");

        System.out.println(ADDING_TO_BASKET_PRODUCT_QUANTITY.getMessage());
        int quantity = InputManager.getIntInput();

        if (name.trim().isBlank()) {
            System.err.println(ERROR_ADDING_TO_BASKET_WRONG_NAME.getMessage());
        }
        else if (quantity <= 0) {
            System.err.println(ERROR_ADDING_TO_BASKET_WRONG_QUANTITY.getMessage());
        } else {
            productRepository.findByName(name)
                    .ifPresentOrElse(product -> basket.addItem(product, quantity),
                            () -> System.err.println(ERROR_NO_SUCH_ITEM_IN_STORE.getMessage() + name));
                                                                     //TODO check if there is enough quantity of product
            System.out.println("Added " + quantity + " " + name + " to basket.");
        }
    }

    private static void printMenu() {
        System.out.println(MENU_OPTIONS.getMessage());
        for (MainMenuOptions option : MainMenuOptions.values()) {
            System.out.println(option.getId() + ". " + option.getMessage());
        }
    }

    private static void viewListOfAllProducts() {
        System.out.println(COLUMNS_OF_PRODUCTS_LISTING.getMessage());
        for (Product product : productRepository.findAll()) {
            System.out.println(product);                                    //TODO add method for matching column elements
        }

    }

    private static void viewItemsInBasket(Map<Product, Integer> basketItems) {
        for (Product product : basketItems.keySet()) {
            System.out.println("Item: " + product.getName() + " in " + basketItems.get(product) + " quantity.");
        }
    }
}
