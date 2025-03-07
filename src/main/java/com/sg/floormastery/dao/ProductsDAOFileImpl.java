package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ProductsDAOFileImpl implements ProductsDAO{
    private HashMap<String, Product> storage = new HashMap<>();
    public String PRODUCTS_FILE = "Files/Products/Products.txt";
    final static String DELIMITER = ",";

    @Override
    public List<Product> getAllProducts() {
        importFromFile();
        return  new ArrayList<>(storage.values());
    }

    @Override
    public HashSet<String> getAllProductTypes() throws PersistanceException{
        HashSet<String> types = new HashSet<>();
        importFromFile();
        for(Product product : storage.values()){
            types.add(product.getProductType());
        }
        return types;
    }

    @Override
    public Product getProduct(String productType) {
        return storage.get(productType);
    }

    public void importFromFile() {
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));
            storage.clear();
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String[] fields = s.split(DELIMITER);
                Product product = new Product(fields[0], new BigDecimal(fields[1]), new BigDecimal(fields[2]));
                storage.put(fields[0], product);
            }
        }
        catch (IOException | NumberFormatException e ) {
            throw new PersistanceException("ERROR: Problem reading the products file");
        }
    }

    public void exportProductsDataToFile(String file) throws PersistanceException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) { // Append mode
            importFromFile();
            out.println("[PRODUCTS]"); // Section title
            for (Product product : storage.values()) {
                out.println(product.getProductType()+","+product.getCostPerSquareFoot()+","
                +product.getLaborCostPerSquareFoot());
            }
            out.println(); // Blank line for separation
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export products data.");
        }
    }
}
