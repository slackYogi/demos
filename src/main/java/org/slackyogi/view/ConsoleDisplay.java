package org.slackyogi.view;

import org.slackyogi.data.ProductRepository;
import org.slackyogi.model.*;
import org.slackyogi.model.enums.ProductType;
import org.slackyogi.view.enums.MainMenuOption;

import java.util.Map;
import java.util.Optional;
import static org.slackyogi.view.enums.Message.*;

public class ConsoleDisplay {
    private static boolean isWorking;
    private static ProductRepository productRepository = new ProductRepository();
    private static Basket basket = new Basket();
    private static boolean adminUser;

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
            MainMenuOption.fromNumber(InputManager.getIntInput())
                    .ifPresentOrElse(option -> actOnUsersChoice(option),
                            () -> System.err.println(ERROR_ENTER_NUMBER_FROM_RANGE.getMessage()));
        } catch (IllegalArgumentException ex) {
            System.err.println(ERROR_NOT_NUMBER.getMessage());
        }
    }

    private static void actOnUsersChoice(MainMenuOption option) { //TODO Instead of switch create abstract class Page and Pages for each switch case?
        switch (option) {
            case VIEW_LIST_OF_ALL_PRODUCTS:
                viewListOfAllProducts();
                break;
            case PRODUCT_EXISTS_IN_STORE:
                System.out.println(ENTER_SEARCHED_PRODUCT_NAME.getMessage());
                System.out.println((isProductAvailable())
                        ? SEARCHED_PRODUCT_IS_AVAILABLE.getMessage()
                        : NO_SUCH_PRODUCT_AVAILABLE.getMessage());
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
            case ADD_PRODUCT_TO_STORE:
                if (adminUser) {
                    addProductToStore();
                }
                break;
            case EDIT_PRODUCT_IN_STORE:
                if (adminUser) {
                    //TODO
                    System.out.println(2);
                }
                break;
            case REMOVE_PRODUCT_FROM_STORE:
                if (adminUser) {
                    System.out.println(ENTER_PRODUCT_NAME_FOR_DELETION.getMessage());
                    Optional<Product> productToBeDeleted = productRepository.findByName(InputManager.getStringInput().orElse(""));
                    if(productToBeDeleted.isPresent())
                    {
                        productRepository.delete(productToBeDeleted.get());
                    }
                    else {
                        System.out.println(NO_SUCH_PRODUCT_AVAILABLE.getMessage());
                    }
                }
                break;
            case RELOG:
                adminUser = false;
                start();
                break;
            case EXIT:
                isWorking = false;
                break;
            default:
                break;
        }
    }

    private static boolean isProductAvailable() {
        return productRepository.findByName(InputManager.getStringInput().orElse("")).isPresent();
    }

    private static void addProductToStore() {
        viewTypesForNewProducts();
        System.out.println(CREATING_PRODUCT_OF_A_TYPE.getMessage());

        Optional<ProductType> optionalType = ProductType
                .fromString(InputManager.getStringInput()
                        .orElse(""));

        if (optionalType.isPresent()){
            switch (optionalType.get()) {
                case FOOD:
                    Optional<Food> foodProduct = getFoodDataFromUser();
                    if (foodProduct.isPresent()) {
                        productRepository.addProduct(foodProduct.get());
                    } else {
                        System.err.println(ERROR_WRONG_INPUT_PRODUCT_CREATION.getMessage());
                    }
                    break;
                case DRINK:
                    Optional<Drink> drinkProduct = getDrinkDataFromUser();
                    if (drinkProduct.isPresent()) {
                        productRepository.addProduct(drinkProduct.get());
                    } else {
                        System.err.println(ERROR_WRONG_INPUT_PRODUCT_CREATION.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static Optional<Food> getFoodDataFromUser() {
        System.out.println(CREATING_PRODUCT_NAME.getMessage()); //TODO change entering values as one string?
        String name = InputManager.getStringInput().orElse("");

        System.out.println(CREATING_PRODUCT_PRICE.getMessage());
        double price = InputManager.getDoubleInput();

        System.out.println(CREATING_FOOD_MASS.getMessage());
        double mass = InputManager.getDoubleInput();

        if (!name.isBlank() && price > 0 && mass > 0)
            return Optional.of(new Food(name, price, mass));
        return Optional.empty();
    }

    private static Optional<Drink> getDrinkDataFromUser() {      //TODO some generic method?
        System.out.println(CREATING_PRODUCT_NAME.getMessage()); //TODO change entering values as one string?
        String name = InputManager.getStringInput().orElse("");

        System.out.println(CREATING_PRODUCT_PRICE.getMessage());
        double price = InputManager.getDoubleInput();

        System.out.println(CREATING_DRINK_CAPACITY.getMessage());
        double capacity = InputManager.getDoubleInput();

        if (!name.isBlank() && price > 0 && capacity > 0)
            return Optional.of(new Drink(name, price, capacity));
        return Optional.empty();
    }

    private static void viewTypesForNewProducts() {
        System.out.println(CREATING_PRODUCT_AVAILABLE_TYPES.getMessage());
        for (ProductType type: ProductType.values()) {
            System.out.println(type.getDescription());
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
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.requiresAdmin())
                System.out.println(option.getId() + ". " + option.getMessage());
            else {
                if (adminUser) {
                    System.out.println(option.getId() + ". " + option.getMessage());
                }
            }
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
