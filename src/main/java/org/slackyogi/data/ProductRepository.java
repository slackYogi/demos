package org.slackyogi.data;

import org.slackyogi.model.Product;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ProductRepository implements Serializable {

    private static TreeSet<Product> products = new TreeSet<>();
    private FileManager fileManager;

    public ProductRepository() {
        fileManager = new FileManager();
        try {
            products = fileManager.importData()
                    .orElse(new TreeSet<>());
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
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

    public void saveDatabase() {
        try {
            fileManager.exportData(products);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
