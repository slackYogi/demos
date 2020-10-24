package org.slackyogi.data;

import org.slackyogi.model.Drink;
import org.slackyogi.model.Food;
import org.slackyogi.model.Product;
import org.slackyogi.model.enums.ProductType;

import java.sql.*;
import java.util.TreeSet;

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

    private static final String INSERT_FOODS = "INSERT INTO " + TABLE_FOOD +
            '(' + COLUMN_FOOD_NAME + ", " + COLUMN_FOOD_PRICE + ", " + COLUMN_FOOD_MASS +
            ") VALUES (?, ?, ?)";
    private static final String INSERT_DRINKS = "INSERT INTO " + TABLE_DRINK +
            '(' + COLUMN_DRINK_NAME + ", " + COLUMN_DRINK_PRICE + ", " + COLUMN_DRINK_CAPACITY +
            ") VALUES (?, ?, ?)";


    //TODO use Optional
    public TreeSet<Product> queryProducts() {
        TreeSet<Product> products = new TreeSet<>();
        products.addAll(queryFoods());
        products.addAll(queryDrinks());
        return products;
    }

    //TODO use Optional
    public TreeSet<Food> queryFoods() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_FOOD);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            TreeSet<Food> foods = new TreeSet<>();
            while (results.next()) {
                Food food = new Food(ProductType.FOOD);
                food.setId(results.getInt(INDEX_FOOD_ID));
                food.setName(results.getString(INDEX_FOOD_NAME));
                food.setPrice(results.getInt(INDEX_FOOD_PRICE));
                food.setMass(results.getInt(INDEX_FOOD_MASS));
                foods.add(food);
            }
            return foods;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    //TODO use Optional
    public TreeSet<Drink> queryDrinks() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_DRINK);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            TreeSet<Drink> drinks = new TreeSet<>();
            while (results.next()) {
                Drink drink = new Drink(ProductType.DRINK);
                drink.setId(results.getInt(INDEX_DRINK_ID));
                drink.setName(results.getString(INDEX_DRINK_NAME));
                drink.setPrice(results.getInt(INDEX_DRINK_PRICE));
                drink.setCapacity(results.getInt(INDEX_DRINK_CAPACITY));
                drinks.add(drink);
            }
            return drinks;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public void insertFood(String name, double price, double mass) throws SQLException {
        insertIntoFoods.setString(1, name);
        insertIntoFoods.setDouble(2, price);
        insertIntoFoods.setDouble(3, mass);
        int affectedRows = insertIntoFoods.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert food!");
        }
    }

    public void insertDrink(String name, double price, double capacity) throws SQLException {
        insertIntoDrinks.setString(1, name);
        insertIntoDrinks.setDouble(2, price);
        insertIntoDrinks.setDouble(3, capacity);
        int affectedRows = insertIntoDrinks.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert drink!");
        }
    }

//    public void exportData(TreeSet<Product> treeSet) throws IOException {
//        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
//            stream.writeObject(treeSet);
//        } catch (FileNotFoundException ex) {
//            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND + FILE_NAME);
//        } catch (IOException ex) {
//            throw new IOException(ERROR_WRITING_TO_A_FILE + FILE_NAME);
//        }
//    }

//    public Optional<TreeSet<Product>> importData() throws IOException, ClassNotFoundException {
//        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
//            return Optional.of((TreeSet<Product>) stream.readObject());
//        } catch (FileNotFoundException ex) {
//            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND + FILE_NAME);
//        } catch (IOException ex) {
//            throw new IOException(ERROR_READING_FROM_A_FILE + FILE_NAME);
//        } catch (ClassNotFoundException ex) {
//            throw new ClassNotFoundException(ERROR_DESERIALIZING_DATA_TO_A_CLASS.toString());
//        }
//    }

    public void open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoFoods = conn.prepareStatement(INSERT_FOODS);
            insertIntoDrinks = conn.prepareStatement(INSERT_DRINKS);

        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public void close() {
        try {
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
