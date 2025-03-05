package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Scanner;

@Component
public class TaxDAOFileImpl implements TaxDAO{
    public String TAXES_FILE = "Files/Taxes/Taxes.txt";
    final static String DELIMITER = ",";

    @Override
    public Tax addStateTaxEntry(String stateCode, BigDecimal taxRate) {
        return null;
    }

    @Override
    public Tax getTax(String stateCode) {
        return storage.get(stateCode);
    }

    @Override
    public HashSet<String> getStatesAbbreviation() throws PersistanceException{
        HashSet<String> states = new HashSet<>();
        importFromFile();
        for(Tax stateAbbreviation : storage.values()){
            states.add(stateAbbreviation.getStateAbbreviation());
        }
        return states;
    }

    public void importFromFile() throws PersistanceException{
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));
            storage.clear();
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String[] fields = s.split(DELIMITER);
                Tax tax = new Tax(fields[0], fields[1], new BigDecimal(fields[2]));
                storage.put(fields[0], tax);
            }
        }
        catch (FileNotFoundException e){
            throw new PersistanceException("ERROR: There was a problem when opening the taxes file");
        }
        catch (IOException | NumberFormatException e ) {
            throw new PersistanceException("ERROR: Problem reading the taxes file");
        }
    }

    public void exportToFile() {

    }
}
