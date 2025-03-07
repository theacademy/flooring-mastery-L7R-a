package com.sg.floormastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;

    public Tax(String stateAbbreviation, String stateName, BigDecimal taxRate) {
        this.stateAbbreviation = stateAbbreviation;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public String getStateName() {
        return stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(stateAbbreviation, tax.stateAbbreviation) &&
                Objects.equals(stateName, tax.stateName) &&
                Objects.equals(taxRate, tax.taxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateAbbreviation, stateName, taxRate);
    }
}
