package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Tax;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        try {

            // Attempt to open the file for appending data
            PrintWriter out = new PrintWriter(new FileWriter(file, true));

            // Iterate through all tax entries in the storage and write each to the file
            out.println("[TAXES]");
            out.flush();

                out.println(tax.getStateAbbreviation()+","+tax.getStateName()+","+ tax.getTaxRate());

            // Blank line for separation
            out.println();
            out.flush();
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export taxes data.");
        }
    }
}
