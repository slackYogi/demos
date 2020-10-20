package org.slackyogi.view;

import org.slackyogi.data.ProductRepository;
import org.slackyogi.model.Basket;
import org.slackyogi.model.BasketItem;
import org.slackyogi.model.Product;
import org.slackyogi.view.enums.MainMenuOptions;

import java.util.Map;

import static org.slackyogi.view.enums.Messages.*;

public class ConsoleDisplay {
    private static boolean isWorking;
    private static ProductRepository productRepository = new ProductRepository();
    private static Basket basket = new Basket();
    private static boolean adminUser = false;

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
            MainMenuOptions.fromNumber(InputManager.getIntInput())
                    .ifPresentOrElse(option -> actOnUsersChoice(option),
                            () -> System.err.println(ERROR_ENTER_NUMBER_FROM_RANGE.getMessage()));
        } catch (IllegalArgumentException ex) {
            System.err.println(ERROR_NOT_NUMBER.getMessage());
        }
    }

    private static void actOnUsersChoice(MainMenuOptions option) { //TODO Instead of switch create abstract class Page and Pages for each switch case?
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
            case REMOVE_ITEM_FROM_BASKET:
                removeItemFromBasket();
                break;
            case EXIT:
                isWorking = false;
                break;
            default:
                break;
        }
    }

    private static void removeItemFromBasket() {
        BasketItem basketItem = getBasketItemData();            //TODO try to remove model information from view
        if (basketItemHasCorrectData(basketItem)) {
            productRepository.findByName(basketItem.getName())
                    .ifPresentOrElse(product -> basket.removeItem(product, basketItem.getQuantity()),
                            () -> System.err.println(ERROR_NO_SUCH_ITEM_IN_STORE.getMessage() + basketItem.getName()));

            System.out.println("Removed " + basketItem.getQuantity() + " " + basketItem.getName() + " from basket."); //TODO Give proper information if some items remained in basker
        }
    }

    private static void addProductToBasket() {                      // TODO consider extracting this to Order class
        BasketItem basketItem = getBasketItemData();
        if (basketItemHasCorrectData(basketItem)) {
            productRepository.findByName(basketItem.getName())
                    .ifPresentOrElse(product -> basket.addItem(product, basketItem.getQuantity()),
                            () -> System.err.println(ERROR_NO_SUCH_ITEM_IN_STORE.getMessage() + basketItem.getName()));
                                                                                             //TODO check if there is enough quantity of product
            System.out.println("Added " + basketItem.getQuantity() + " " + basketItem.getName() + " to basket."); //TODO add 's' for plural items
        }
    }

    private static boolean basketItemHasCorrectData(BasketItem basketItem) {
        if (basketItem.getName().trim().isBlank()) {
            System.err.println(ERROR_MODIFYING_BASKET_WRONG_NAME.getMessage());
            return false;
        } else if (basketItem.getQuantity() <= 0) {
            System.err.println(ERROR_ADDING_TO_BASKET_WRONG_QUANTITY.getMessage());
            return false;
        } else {
            return true;
        }
    }

    private static BasketItem getBasketItemData() {
        System.out.println(ADDING_TO_BASKET_PRODUCT_NAME.getMessage());
        String name = InputManager.getStringInput().orElse("");

        System.out.println(ADDING_TO_BASKET_PRODUCT_QUANTITY.getMessage());
        int quantity = InputManager.getIntInput();

        return new BasketItem(name, quantity);
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

    private static void loggingIn() {
        System.out.println(LOGGING.getMessage());
        String login = InputManager.getStringInput().orElse("guest");
        if (login.equals("admin")) {
            System.out.println(LOGGED_EMPLOYEE.getMessage());
            adminUser = true;
        } else {
            System.out.println(LOGGED_CLIENT.getMessage());
        }
        System.out.println(WELCOMING.getMessage() + login + "!");
    }
}
