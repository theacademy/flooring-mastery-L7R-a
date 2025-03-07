package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductsDAOFileImpl implements ProductsDAO{
    private HashMap<String, Product> storage = new HashMap<>();
    private final static String PRODUCTS_FILE = "Files/Products/Products.txt";
    private final static String DELIMITER = ",";

    public ProductsDAOFileImpl() {
        importFromFile();
    }

    @Override
    public List<Product> getAllProducts() {
        return  new ArrayList<>(storage.values());
    }

    @Override
    public HashSet<String> getAllProductTypes() throws PersistanceException {
        // Using lambdas and streams in DAO
        return storage.values().stream()
                .map(product -> product.getProductType())
                .collect(Collectors.toCollection(() -> new HashSet<>()));
    }

    @Override
    public Product getProduct(String productType) {
        return storage.get(productType);
    }

    public void importFromFile() {
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));

            // Making sure the hash map is clear before adding new content
            storage.clear();

            while(sc.hasNextLine()){
                // Get the line and its fields
                String s = sc.nextLine();
                String[] fields = s.split(DELIMITER);

                // Create the product object with the data and store it in the hash map
                Product product = new Product(fields[0], new BigDecimal(fields[1]), new BigDecimal(fields[2]));
                storage.put(fields[0], product);
            }
        }
        catch (FileNotFoundException e){
            throw new PersistanceException("ERROR: There was a problem finding the products file");
        }
        catch (IOException | NumberFormatException e ) {
            throw new PersistanceException("ERROR: Problem reading the products file");
        }
    }

    public void exportProductsDataToFile(String file) throws PersistanceException {
        try {
            // Attempt to open the file for appending data
            PrintWriter out = new PrintWriter(new FileWriter(file, true));

            // Iterate through all products entries in the storage and write each to the file
            out.println("[PRODUCTS]");
            for (Product product : storage.values()) {
                out.println(product.getProductType()+","+product.getCostPerSquareFoot()+","
                +product.getLaborCostPerSquareFoot());
            }

            // Blank line for separation
            out.println();
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export products data.");
        }
    }
}
