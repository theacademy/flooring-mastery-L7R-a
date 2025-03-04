package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Tax;

import java.math.BigDecimal;
import java.util.HashMap;

public interface TaxDAO {
    HashMap<String, Tax> storage = new HashMap<>();
    Tax addStateTaxEntry(String stateCode, BigDecimal taxRate);
    Tax getState(String stateCode);

}
