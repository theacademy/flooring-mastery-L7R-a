package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

@Component
public class ProductsDAOFileImpl implements ProductsDAO{
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

    public void exportToFile() {

    }
}
