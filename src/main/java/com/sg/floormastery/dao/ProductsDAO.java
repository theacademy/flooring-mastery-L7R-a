package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;

import java.util.HashMap;
import java.util.List;

public interface ProductsDAO {
    HashMap<String, Product> storage = new HashMap<>();
    List<Product> getAllProducts();

}
