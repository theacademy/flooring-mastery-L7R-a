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
        // Assuming the DAO takes a file path for initialization
        testDao = new ProductsDAOFileImpl();
    }

    @Test
    void getAllProducts() {
        List<Product> products = testDao.getAllProducts();
        assertNotNull(products, "Product list should not be null");
        assertFalse(products.isEmpty(), "Product list should not be empty");
    }

    @Test
    void getAllProductTypes() {
        Set<String> productTypes = testDao.getAllProductTypes();
        assertNotNull(productTypes, "Product types set should not be null");
        assertFalse(productTypes.isEmpty(), "Product types set should not be empty");
        assertEquals(4, productTypes.size(), "There should be 4 types");
        assertTrue(productTypes.contains("Wood"),
                "Product types should wood as a type");
        assertTrue(productTypes.contains("Laminate"),
                "Product types should laminate as a type");
        assertTrue(productTypes.contains("Carpet"),
                "Product types should carpet as a type");
        assertTrue(productTypes.contains("Tile"),
                "Product types should tile as a type");
    }

    @Test
    void getProduct() {
        Product product = testDao.getProduct("Wood");
        assertNotNull(product, "Product should not be null");
        assertEquals("Wood", product.getProductType(), "Product type should match requested type");
    }

    @Test
    void importFromFile() {
        assertDoesNotThrow(() -> testDao.importFromFile(),
                "Importing from file should not throw an exception");

        // Verify some data was imported
        assertFalse(testDao.getAllProducts().isEmpty(), "Product list should not be empty after import");
        assertEquals(4, testDao.getAllProducts().size(), "There should be 4 types");

    }

}