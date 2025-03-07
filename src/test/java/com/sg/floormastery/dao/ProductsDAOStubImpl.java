package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;

import java.util.HashSet;
import java.util.List;

public class ProductsDAOStubImpl implements ProductsDAO {
    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public HashSet<String> getAllProductTypes() throws PersistanceException {
        return null;
    }

    @Override
    public Product getProduct(String productType) {
        return null;
    }

    @Override
    public void exportProductsDataToFile(String file) {

    }
}
