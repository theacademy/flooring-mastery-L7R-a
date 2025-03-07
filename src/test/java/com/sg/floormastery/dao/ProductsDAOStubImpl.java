package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductsDAOStubImpl implements ProductsDAO {
    Product product;

    public ProductsDAOStubImpl() {
        product = new Product("Cardboard", new BigDecimal("12.5"), new BigDecimal("16.33"));
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        list.add(product);
        return list;
    }

    @Override
    public HashSet<String> getAllProductTypes() throws PersistanceException {
        HashSet<String> products = new HashSet<>();
        products.add(product.getProductType());
        return products;
    }

    @Override
    public Product getProduct(String productType) {
        if(product.getProductType().equals(productType)){
            return product;
        }
        else{
            return null;
        }    }

    @Override
    public void exportProductsDataToFile(String file) {
        try {
            // Attempt to open the file for appending data
            PrintWriter out = new PrintWriter(new FileWriter(file, true));

            // Iterate through all products entries in the storage and write each to the file
            out.println("[PRODUCTS]");
            out.flush();
                out.println(product.getProductType()+","+product.getCostPerSquareFoot()+","
                        +product.getLaborCostPerSquareFoot());

            // Blank line for separation
            out.println();
            out.flush();
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export products data.");
        }
    }
}
