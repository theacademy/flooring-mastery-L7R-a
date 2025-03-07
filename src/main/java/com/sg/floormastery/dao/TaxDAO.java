package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Tax;

import java.util.HashSet;

public interface TaxDAO {
    Tax getTax(String stateCode);
    HashSet<String> getStatesAbbreviation();
    void exportTaxesDataToFile(String file);

}
