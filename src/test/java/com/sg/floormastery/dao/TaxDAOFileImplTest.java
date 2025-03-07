package com.sg.floormastery.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
class TaxDAOFileImplTest {

    private TaxDAOFileImpl testDao;

    @BeforeEach
    void setUp() throws Exception {
        testDao = new TaxDAOFileImpl();
    }

    @Test
    void getTax() {
        BigDecimal taxRate = testDao.getTax("TX").getTaxRate();
        assertNotNull(taxRate, "Tax rate should not be null for a valid state");
        assertTrue(taxRate.compareTo(BigDecimal.ZERO) > 0, "Tax rate should be greater than zero");
    }

    @Test
    void getStatesAbbreviation() {
        Set<String> states = testDao.getStatesAbbreviation();
        assertNotNull(states, "States set should not be null");
        assertFalse(states.isEmpty(), "States set should not be empty");
        assertTrue(states.contains("TX"), "Set should contain Texas abbreviation");
        assertTrue(states.contains("WA"), "Set should contain Washington state abbreviation");
        assertTrue(states.contains("CA"), "Set should contain California state abbreviation");
        assertTrue(states.contains("KY"), "Set should contain Kentucky state abbreviation");
        assertEquals(4, states.size(), "Should have 4 states");


    }

    @Test
    void importFromFile() {
        assertDoesNotThrow(() -> testDao.importFromFile(), "Importing from file should not throw an exception");

        // Verify some data was imported
        assertFalse(testDao.getStatesAbbreviation().isEmpty(), "States should not be empty after import");
    }

}