package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Tax;

import java.util.HashSet;

public class TaxesDAOStubImpl implements TaxDAO {
    @Override
    public Tax getTax(String stateCode) {
        return null;
    }

    @Override
    public HashSet<String> getStatesAbbreviation() {
        return null;
    }

    @Override
    public void exportTaxesDataToFile(String file) {

    }
}
