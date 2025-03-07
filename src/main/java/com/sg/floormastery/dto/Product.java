package com.sg.floormastery.dto;

import java.math.BigDecimal;

public class Product {
    private String ProductType;
    private BigDecimal CostPerSquareFoot;
    private BigDecimal LaborCostPerSquareFoot;

    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        ProductType = productType;
        CostPerSquareFoot = costPerSquareFoot;
        LaborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public String getProductType() {
        return ProductType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return CostPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return LaborCostPerSquareFoot;
    }

}
