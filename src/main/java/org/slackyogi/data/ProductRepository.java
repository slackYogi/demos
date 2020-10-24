package org.slackyogi.data;

import org.slackyogi.model.Product;

import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public class ProductRepository implements Serializable {

    private static TreeSet<Product> products = new TreeSet<>();
    private DatabaseManager databaseManager;

    public ProductRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        databaseManager.open();
        databaseManager.populateDB();

        products = databaseManager.queryProducts();

        //TODO import products in store
//        try {
//            products = dataManager.importData()
//                    .orElse(new TreeSet<>());
//        } catch (IOException ex) {
//            ex.getMessage();
//        } catch (ClassNotFoundException ex) {
//            ex.getMessage();
//        }
    }

    public void addProduct(Product product) {
        if (product != null)
            products.add(product);
    }

    public Optional<Product> findByName(String name) {
        for (Product product : findAll()) {
            if (product.getName().equals(name)) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public SortedSet<Product> findAll() {
        return Collections.unmodifiableSortedSet(products);
    }

    public Long count() {
        return Long.valueOf(findAll().size());
    }

    public void delete(Product product) {
        if (product != null) {
            if(findByName(product.getName()).isPresent())
            {
                products.remove(product);
            }
        }
    }

    public void update(Product oldProduct, Product newProduct) {
        if (newProduct != null && products.contains(oldProduct)) {
            products.remove(oldProduct);
            products.add(newProduct);
        }
    }

    public void close() {
        databaseManager.close();
    }
}
