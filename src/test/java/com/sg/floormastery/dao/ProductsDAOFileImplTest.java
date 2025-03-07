package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class ProductsDAOFileImplTest {

    private ProductsDAOFileImpl testDao;

    @BeforeEach
    void setUp() throws Exception {
        testDao = new ProductsDAOFileImpl();
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = testDao.getAllProducts();
        assertNotNull(products, "Product list should not be null");
        assertFalse(products.isEmpty(), "Product list should not be empty");
    }

    @Test
    void testGetAllProductTypes() {
        Set<String> productTypes = testDao.getAllProductTypes();
        assertNotNull(productTypes, "Product types set should not be null");
        assertFalse(productTypes.isEmpty(), "Product types set should not be empty");
        assertEquals(4, productTypes.size(), "There should be 4 types");
        assertTrue(productTypes.contains("Wood"),
                "Product types should have wood as a type");
        assertTrue(productTypes.contains("Laminate"),
                "Product types should have laminate as a type");
        assertTrue(productTypes.contains("Carpet"),
                "Product types should have carpet as a type");
        assertTrue(productTypes.contains("Tile"),
                "Product types should have tile as a type");
    }

    @Test
    void testGetProduct() {
        Product product = testDao.getProduct("Wood");
        assertNotNull(product, "Product should not be null");
        assertEquals("Wood", product.getProductType(),
                "Product type should match requested type");
        assertEquals(new BigDecimal("5.15"), product.getCostPerSquareFoot(),
                "Product cost per sq ft should match");
        assertEquals(new BigDecimal("4.75"), product.getLaborCostPerSquareFoot(),
                "Product labor cost per sq ft should match");
    }

    @Test
    void testImportFromFileGetProduct() {
        assertDoesNotThrow(() -> testDao.importFromFile(),
                "Importing from file should not throw an exception");

        // Verify some data was imported
        assertFalse(testDao.getAllProducts().isEmpty(), "Product list should not be empty after import");
        assertEquals("Wood", testDao.getProduct("Wood").getProductType()
                , "It should contain the wood product type");
        assertEquals("Tile", testDao.getProduct("Tile").getProductType()
                , "It should contain the tile product type");
        assertEquals("Laminate", testDao.getProduct("Laminate").getProductType()
                , "It should contain the laminate product type");
        assertEquals("Carpet", testDao.getProduct("Carpet").getProductType()
                , "It should contain the carpet product type");
        assertEquals(4, testDao.getAllProducts().size(), "There should be 4 types");
    }




}