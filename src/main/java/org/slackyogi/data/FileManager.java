package org.slackyogi.data;
import org.slackyogi.model.Product;
import java.io.*;
import java.util.Optional;
import java.util.TreeSet;

public class FileManager {
    private static final String FILE_NAME = "file_database.dat";

    public void exportData(TreeSet<Product> treeSet) { //TODO some other type to encapsulate model?
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME)))
        {
            stream.writeObject(treeSet);
        } catch (IOException ex) { //TODO custom exceptions?
            ex.getStackTrace();
        }
    }

    public Optional<TreeSet<Product>> importData() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return Optional.of((TreeSet<Product>) stream.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            ex.getStackTrace();
        }
        return Optional.empty();
    }
}
