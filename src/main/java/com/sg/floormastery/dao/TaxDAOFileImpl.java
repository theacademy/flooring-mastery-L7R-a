package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Tax;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class TaxDAOFileImpl implements TaxDAO{
    private HashMap<String, Tax> storage = new HashMap<>();
    private final static String TAXES_FILE = "Files/Taxes/Taxes.txt";
    private final static String DELIMITER = ",";

    public TaxDAOFileImpl() {
        importFromFile();
    }

    @Override
    public Tax getTax(String stateCode) {
        return storage.get(stateCode);
    }

    @Override
    public HashSet<String> getStatesAbbreviation() throws PersistanceException{
        // Using lambdas and streams in DAO
        return storage.values().stream()
                        .map(tax -> tax.getStateAbbreviation())
                        .collect(Collectors.toCollection(() -> new HashSet<>()));
    }

    public void importFromFile() throws PersistanceException{
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(TAXES_FILE)));

            // Making sure the hash map is clear before adding new content
            storage.clear();

            while(sc.hasNextLine()){
                // Get the line and its fields
                String s = sc.nextLine();
                String[] fields = s.split(DELIMITER);

                // Create the tax object with the data and store it in the hash map
                Tax tax = new Tax(fields[0], fields[1], new BigDecimal(fields[2]));
                storage.put(fields[0], tax);
            }
        }
        catch (FileNotFoundException e){
            throw new PersistanceException("ERROR: There was a problem finding the taxes file");
        }
        catch (IOException | NumberFormatException e ) {
            throw new PersistanceException("ERROR: Problem reading the taxes file");
        }
    }

    public void exportTaxesDataToFile(String file) throws PersistanceException {
        try {

            // Attempt to open the file for appending data
            PrintWriter out = new PrintWriter(new FileWriter(file, true));

            // Iterate through all tax entries in the storage and write each to the file
            out.println("[TAXES]");
            out.flush();

            for (Tax tax : storage.values()) {
                out.println(tax.getStateAbbreviation()+","+tax.getStateName()+","+ tax.getTaxRate());
            }

            // Blank line for separation
            out.println();
            out.flush();
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export taxes data.");
        }
    }
}
