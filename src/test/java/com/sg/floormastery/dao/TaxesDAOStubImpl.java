package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Tax;

import java.math.BigDecimal;
import java.util.HashSet;

public class TaxesDAOStubImpl implements TaxDAO {
    public Tax tax;
    public TaxesDAOStubImpl() {
        tax = new Tax("FL", "Florida", new BigDecimal("1.8"));
    }

    @Override
    public Tax getTax(String stateCode) {
        if(tax.getStateAbbreviation().equals(stateCode)){
            return tax;
        }
        else{
            return null;
        }
    }

    @Override
    public HashSet<String> getStatesAbbreviation() {
        HashSet<String> states = new HashSet<>();
        states.add(tax.getStateAbbreviation());
        return states;
    }

    @Override
    public void exportTaxesDataToFile(String file) {

    }
}
