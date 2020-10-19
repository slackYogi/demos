package org.slackyogi.view;

import org.slackyogi.data.ProductRepository;
import org.slackyogi.model.Product;
import org.slackyogi.view.enums.MainMenuOptions;

import static org.slackyogi.view.enums.Messages.*;

public class ConsoleDisplay {
    private static boolean isWorking;
    private static ProductRepository productRepository = new ProductRepository();

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
                for (Product product : productRepository.findAll()){
                    System.out.println(product);
            }
                break;
            case ADD_PRODUCT_TO_BASKET:
                break;
            case EXIT:
                isWorking = false;
                break;
            default:
                break;
        }
    }

    private static void printMenu() {
        System.out.println(MENU_OPTIONS.getMessage());
        for (MainMenuOptions option : MainMenuOptions.values()) {
            System.out.println(option.getId() + ". " + option.getMessage());
        }
    }


}
