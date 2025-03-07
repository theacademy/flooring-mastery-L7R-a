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
    void testGetTax() {
        BigDecimal taxRate = testDao.getTax("TX").getTaxRate();
        assertNotNull(taxRate, "Tax rate should not be null for a valid state");
        assertEquals( "TX", testDao.getTax("TX").getStateAbbreviation() ,
                "Should have the Texas abbreviation");
        assertEquals( "Texas", testDao.getTax("TX").getStateName() ,
                "Should have the Texas state name");
        assertEquals( new BigDecimal("4.45"), testDao.getTax("TX").getTaxRate() ,
                "Should have the tax rate");
    }

    @Test
    void testGetStatesAbbreviation() {
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
    void testImportFromFileGetStatesAbbreviation() {
        assertDoesNotThrow(() -> testDao.importFromFile(), "Importing from file should not throw an exception");

        // Verify some data was imported
        assertFalse(testDao.getStatesAbbreviation().isEmpty(), "States should not be empty after import");
        assertTrue(testDao.getStatesAbbreviation().contains("TX")
                , "It should contain the Texas state");
        assertTrue(testDao.getStatesAbbreviation().contains("WA")
                , "It should contain the Washington state");
        assertTrue(testDao.getStatesAbbreviation().contains("CA")
                , "It should contain the California state");
        assertTrue(testDao.getStatesAbbreviation().contains("KY")
                , "It should contain the Kentucky state");
        assertEquals(4, testDao.getStatesAbbreviation().size(), "There should be 4 types");
    }

    @Test
    void testExportTaxesDataToFile(){
        // Specify the path where the file should be created
        String filePath = "Files/TestExportData/ExportTaxes.txt";
        testDao.importFromFile();
        assertDoesNotThrow(() ->
                        testDao.exportTaxesDataToFile(filePath)
                , "Exporting to file should not throw an exception");
    }
}