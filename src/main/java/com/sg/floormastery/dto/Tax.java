package com.sg.floormastery.dto;

import java.math.BigDecimal;

public class Tax {
    private String StateAbbreviation;
    private String StateName;
    private BigDecimal TaxRate;

    public Tax(String stateAbbreviation, String stateName, BigDecimal taxRate) {
        StateAbbreviation = stateAbbreviation;
        StateName = stateName;
        TaxRate = taxRate;
    }

    public String getStateAbbreviation() {
        return StateAbbreviation;
    }

    public String getStateName() {
        return StateName;
    }

    public BigDecimal getTaxRate() {
        return TaxRate;
    }

}
