package org.slackyogi.data;

import org.slackyogi.model.Drink;
import org.slackyogi.model.Food;
import org.slackyogi.model.Product;
import org.slackyogi.model.enums.ProductType;

import java.sql.*;
import java.util.Optional;
import java.util.TreeSet;

import static org.slackyogi.view.enums.Message.*;

public class DatabaseManager {
    private static final String DATABASE_NAME = "database.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:src\\main\\resources\\" + DATABASE_NAME;
    private static Connection conn;

    private static final String TABLE_FOOD = "foods";
    private static final String COLUMN_FOOD_ID = "_id";
    private static final String COLUMN_FOOD_NAME = "name";
    private static final String COLUMN_FOOD_PRICE = "price";
    private static final String COLUMN_FOOD_MASS = "mass";
    private static final int INDEX_FOOD_ID = 1;
    private static final int INDEX_FOOD_NAME = 2;
    private static final int INDEX_FOOD_PRICE = 3;
    private static final int INDEX_FOOD_MASS = 4;

    private static final String TABLE_DRINK = "drinks";
    private static final String COLUMN_DRINK_ID = "_id";
    private static final String COLUMN_DRINK_NAME = "name";
    private static final String COLUMN_DRINK_PRICE = "price";
    private static final String COLUMN_DRINK_CAPACITY = "capacity";
    private static final int INDEX_DRINK_ID = 1;
    private static final int INDEX_DRINK_NAME = 2;
    private static final int INDEX_DRINK_PRICE = 3;
    private static final int INDEX_DRINK_CAPACITY = 4;

    private PreparedStatement insertIntoFoods;
    private PreparedStatement insertIntoDrinks;
    private PreparedStatement queryFoodsByName;
    private PreparedStatement queryDrinksByName;
    private PreparedStatement deleteFoodsByName;
    private PreparedStatement deleteDrinksByName;
    private PreparedStatement updateFoodsByName;
    private PreparedStatement updateDrinksByName;

    private static final String INSERT_FOODS = "INSERT INTO " + TABLE_FOOD +
            '(' + COLUMN_FOOD_NAME + ", " + COLUMN_FOOD_PRICE + ", " + COLUMN_FOOD_MASS +
            ") VALUES (?, ?, ?)";
    private static final String INSERT_DRINKS = "INSERT INTO " + TABLE_DRINK +
            '(' + COLUMN_DRINK_NAME + ", " + COLUMN_DRINK_PRICE + ", " + COLUMN_DRINK_CAPACITY +
            ") VALUES (?, ?, ?)";
    private static final String QUERY_FOODS =
            "SELECT * FROM " + TABLE_FOOD + " WHERE " + COLUMN_FOOD_NAME + " = ?";
    private static final String QUERY_DRINKS =
            "SELECT * FROM " + TABLE_DRINK + " WHERE " + COLUMN_DRINK_NAME + " = ?";
    private static final String DELETE_FOODS_BY_NAME =
            "DELETE FROM " + TABLE_FOOD + " WHERE " + COLUMN_FOOD_NAME + " = ?";
    private static final String DELETE_DRINKS_BY_NAME =
            "DELETE FROM " + TABLE_DRINK + " WHERE " + COLUMN_DRINK_NAME + " = ?";
    private static final String UPDATE_DRINKS_BY_NAME =
            "UPDATE " + TABLE_DRINK
                    + " SET " + COLUMN_DRINK_NAME + " = ?, "
                    + COLUMN_DRINK_PRICE + " = ?, "
                    + COLUMN_DRINK_CAPACITY + " = ? "
                    + "WHERE " + COLUMN_DRINK_NAME + " = ?";
    private static final String UPDATE_FOODS_BY_NAME =
            "UPDATE " + TABLE_FOOD
                    + " SET " + COLUMN_FOOD_NAME + " = ?, "
                    + COLUMN_FOOD_PRICE + " = ?, "
                    + COLUMN_FOOD_MASS + " = ? "
                    + "WHERE " + COLUMN_DRINK_NAME + " = ?";
    private static final String SELECT_ALL = "SELECT * FROM ";


    public void updateFood(String name, Food newFood) throws SQLException {
        if (queryFoodsByName(name).isPresent()) {
            try {
                updateFoodsByName.setString(1, newFood.getName());
                updateFoodsByName.setDouble(2, newFood.getPrice());
                updateFoodsByName.setDouble(3, newFood.getMass());
                updateFoodsByName.setString(4, name);
                updateFoodsByName.execute();
            } catch (SQLException ex) {
                throw new SQLException(DATABASE_PRODUCT_NOT_UPDATED + newFood.getType().toString());
            }
        }
    }

    public void updateDrink(String name, Drink newDrink) throws SQLException {
        if (queryDrinksByName(name).isPresent()) {
            try {
                updateDrinksByName.setString(1, newDrink.getName());
                updateDrinksByName.setDouble(2, newDrink.getPrice());
                updateDrinksByName.setDouble(3, newDrink.getCapacity());
                updateDrinksByName.setString(4, name);
                updateDrinksByName.execute();
            } catch (SQLException ex) {
                throw new SQLException(DATABASE_PRODUCT_NOT_UPDATED + newDrink.getType().toString());
            }
        }
    }

    public void deleteFood(String name) throws SQLException {
        if (queryFoodsByName(name).isPresent()) {
            deleteFoodsByName.setString(1, name);
            deleteFoodsByName.execute();
        }
    }

    public void deleteDrink(String name) throws SQLException {
        if (queryDrinksByName(name).isPresent()) {
            deleteDrinksByName.setString(1, name);
            deleteDrinksByName.execute();
        }
    }

    public Optional<Drink> queryDrinksByName(String name) {
        Drink drink = new Drink(ProductType.DRINK);
        try {
            queryDrinksByName.setString(1, name);
            ResultSet result = queryDrinksByName.executeQuery();
            if (!result.isClosed()) {
                drink.setName(result.getString(2));
                drink.setPrice(result.getDouble(3));
                drink.setCapacity(result.getDouble(4));
                return Optional.of(drink);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Food> queryFoodsByName(String name) {
        Food food = new Food(ProductType.FOOD);
        try {
            queryFoodsByName.setString(1, name);
            ResultSet result = queryFoodsByName.executeQuery();
            if (!result.isClosed()) {
                food.setName(result.getString(2));
                food.setPrice(result.getDouble(3));
                food.setMass(result.getDouble(4));
                return Optional.of(food);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<TreeSet<Product>> queryProducts() {
        TreeSet<Product> products = new TreeSet<>();
        queryFoods().ifPresent(products::addAll);
        queryDrinks().ifPresent(products::addAll);
        if (!products.isEmpty()) {
            return Optional.of(products);
        } else {
            return Optional.empty();
        }
    }

    public Optional<TreeSet<Food>> queryFoods() {
        StringBuilder sb = new StringBuilder(SELECT_ALL);
        sb.append(TABLE_FOOD);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            TreeSet<Food> foods = new TreeSet<>();
            while (results.next()) {
                Food food = new Food(ProductType.FOOD);
                food.setId(results.getInt(INDEX_FOOD_ID));
                food.setName(results.getString(INDEX_FOOD_NAME));
                food.setPrice(results.getDouble(INDEX_FOOD_PRICE));
                food.setMass(results.getInt(INDEX_FOOD_MASS));
                foods.add(food);
            }
            if (!foods.isEmpty()) {
                return Optional.of(foods);
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<TreeSet<Drink>> queryDrinks() {
        StringBuilder sb = new StringBuilder(SELECT_ALL);
        sb.append(TABLE_DRINK);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            TreeSet<Drink> drinks = new TreeSet<>();
            while (results.next()) {
                Drink drink = new Drink(ProductType.DRINK);
                drink.setId(results.getInt(INDEX_DRINK_ID));
                drink.setName(results.getString(INDEX_DRINK_NAME));
                drink.setPrice(results.getDouble(INDEX_DRINK_PRICE));
                drink.setCapacity(results.getInt(INDEX_DRINK_CAPACITY));
                drinks.add(drink);
            }
            if (!drinks.isEmpty()) {
                return Optional.of(drinks);
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public void insertFood(String name, double price, double mass) throws SQLException {
        insertIntoFoods.setString(1, name);
        insertIntoFoods.setDouble(2, price);
        insertIntoFoods.setDouble(3, mass);
        int affectedRows = insertIntoFoods.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException(DATABASE_ERROR_INSERTING_PRODUCT.toString());
        }
    }

    public void insertDrink(String name, double price, double capacity) throws SQLException {
        insertIntoDrinks.setString(1, name);
        insertIntoDrinks.setDouble(2, price);
        insertIntoDrinks.setDouble(3, capacity);
        int affectedRows = insertIntoDrinks.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException(DATABASE_ERROR_INSERTING_PRODUCT.toString());
        }
    }

    public void open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoFoods = conn.prepareStatement(INSERT_FOODS);
            insertIntoDrinks = conn.prepareStatement(INSERT_DRINKS);
            queryFoodsByName = conn.prepareStatement(QUERY_FOODS);
            queryDrinksByName = conn.prepareStatement(QUERY_DRINKS);
            deleteDrinksByName = conn.prepareStatement(DELETE_DRINKS_BY_NAME);
            deleteFoodsByName = conn.prepareStatement(DELETE_FOODS_BY_NAME);
            updateFoodsByName = conn.prepareStatement(UPDATE_FOODS_BY_NAME);
            updateDrinksByName = conn.prepareStatement(UPDATE_DRINKS_BY_NAME);
            System.out.println();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public void close() {
        try {

            if (updateFoodsByName != null)
                updateFoodsByName.close();
            if (updateDrinksByName != null)
                updateDrinksByName.close();
            if (deleteDrinksByName != null)
                deleteDrinksByName.close();
            if (deleteFoodsByName != null)
                deleteFoodsByName.close();
            if (queryDrinksByName != null)
                queryDrinksByName.close();
            if (queryFoodsByName != null)
                queryFoodsByName.close();
            if (insertIntoFoods != null)
                insertIntoFoods.close();
            if (insertIntoDrinks != null)
                insertIntoDrinks.close();
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public void populateDB() {
        try {
            Statement statement = conn.createStatement();
            createFoodsTable(statement);
            createDrinksTable(statement);
            insertFood("Pizza", 12, 1.5);
            insertFood("Potato", 0.3, 0.2);
            insertFood("Egg", 0.5, 0.1);
            insertFood("Ham", 8.2, 0.5);
            insertFood("Pasta", 4.5, 1);
            insertFood("Tomato", 3.9, 1);
            insertFood("Banana", 5.2, 1);
            insertFood("Olives", 3.3, 0.5);
            insertFood("Chocolate", 5, 0.2);
            insertFood("Chips", 4.5, 0.3);
            insertFood("Fries", 6.2, 1.1);
            insertDrink("Cola", 5.3, 2.0);
            insertDrink("Soda", 3.7, 1.5);
            insertDrink("Water", 2.3, 1.5);
            insertDrink("Pepsi", 4.1, 1.0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void createFoodsTable(Statement statement) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS foods");
        statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_FOOD + " ("
                + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_FOOD_NAME + " TEXT NOT NULL, "
                + COLUMN_FOOD_PRICE + " REAL, "
                + COLUMN_FOOD_MASS + " REAL)");
    }

    private static void createDrinksTable(Statement statement) throws SQLException {
        statement.execute("DROP TABLE IF EXISTS drinks");
        statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_DRINK + " ("
                + COLUMN_DRINK_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_DRINK_NAME + " TEXT NOT NULL, "
                + COLUMN_DRINK_PRICE + " REAL, "
                + COLUMN_DRINK_CAPACITY + " REAL)");
    }
}
