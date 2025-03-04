package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class ProductsDAOFileImpl implements ProductsDAO{
    public String PRODUCTS_FILE = "Files/Products/Products.txt";
    final static String DELIMITER = ",";

    @Override
    public List<Product> getAllProducts() {
        return List.of();
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
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void exportToFile() {

    }
}
