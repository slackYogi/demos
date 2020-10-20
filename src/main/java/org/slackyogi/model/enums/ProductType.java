package org.slackyogi.model.enums;

import java.util.Optional;

public enum ProductType {
    FOOD("Food"),
    DRINK("Drink");

    private String description;

    public String getDescription() {
        return description;
    }

    ProductType(String description) {
        this.description = description;
    }

    public static Optional<ProductType> fromString(String input) {
        for (ProductType type : ProductType.values()) {
            if (type.description.equals(input)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }


}
