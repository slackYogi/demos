package org.slackyogi.data;

import org.slackyogi.model.Drink;
import org.slackyogi.model.Food;
import org.slackyogi.model.Product;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.TreeSet;

public class ProductRepository implements Serializable {

    private static TreeSet<Product> products = new TreeSet<>();
    private DatabaseManager databaseManager;

    public ProductRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        databaseManager.open();
        databaseManager.populateDB();
        databaseManager.queryProducts().ifPresent(prod -> products = prod);
    }

    public void addProduct(Product product) throws SQLException {
        switch (product.getType()) {
            case FOOD:
                Food food = (Food) product;
                databaseManager.insertFood(food.getName(), food.getPrice(), food.getMass());
                break;
            case DRINK:
                Drink drink = (Drink) product;
                databaseManager.insertDrink(drink.getName(), drink.getPrice(), drink.getCapacity());
                break;
            default:
                break;
        }
    }

    public Optional<Product> findByName(String name) {
        Optional<Food> food = databaseManager.queryFoodsByName(name);
        Optional<Drink> drink = databaseManager.queryDrinksByName(name);
        if (food.isPresent()) {
            return Optional.of(food.get());
        } else if (drink.isPresent()) {
            return Optional.of(drink.get());
        } else {
            System.out.println();
            return Optional.empty();
        }
    }

    public Optional<TreeSet<Product>> findAll() {
        return databaseManager.queryProducts();
    }

    public int count() {
        Optional<TreeSet<Product>> products = findAll();
        if (products.isPresent()) {
            return products.get().size();
        } else {
            return 0;
        }
    }

    public void delete(Product product) {
        if (product != null) {
           if (product instanceof Food)
               delete((Food)product);
           if (product instanceof Drink)
               delete((Drink)product);
        }
    }

    public void delete(Food food) {
        if (findByName(food.getName()).isPresent()) {
            try {
                databaseManager.deleteFood(food.getName());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void delete(Drink drink) {
        if (findByName(drink.getName()).isPresent()) {
            try {
                databaseManager.deleteDrink(drink.getName());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void update(String oldFoodName, Food newFood) {
        if (newFood != null) {
            try {
                databaseManager.updateFood(oldFoodName, newFood);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void update(String oldDrinkName, Drink newDrink) {
        if (newDrink != null) {
            try {
                databaseManager.updateDrink(oldDrinkName, newDrink);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        databaseManager.close();
    }
}
