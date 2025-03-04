package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
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
    public Tax getState(String stateCode) {
        return null;
    }

    public void importFromFile() {
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
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void exportToFile() {

    }
}
