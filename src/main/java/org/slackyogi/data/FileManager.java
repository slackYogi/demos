package org.slackyogi.data;
import org.slackyogi.model.Product;

import java.io.*;
import java.util.Optional;
import java.util.TreeSet;

import static org.slackyogi.view.enums.Message.*;

public class FileManager {
    private static final String FILE_NAME = "file_database.dat";

    public void exportData(TreeSet<Product> treeSet) throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(FILE_NAME)))
        {
            stream.writeObject(treeSet);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND + FILE_NAME);
        } catch (IOException ex) {
            throw new IOException(ERROR_WRITING_TO_A_FILE + FILE_NAME);
        }
    }

    public Optional<TreeSet<Product>> importData() throws IOException, ClassNotFoundException {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return Optional.of((TreeSet<Product>) stream.readObject());
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND + FILE_NAME);
        } catch (IOException ex) {
            throw new IOException(ERROR_READING_FROM_A_FILE + FILE_NAME);
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException(ERROR_DESERIALIZING_DATA_TO_A_CLASS.toString());
        }
    }
}
