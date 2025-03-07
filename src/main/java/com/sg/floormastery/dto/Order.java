package com.sg.floormastery.dto;

import java.math.BigDecimal;

public class Order {
    private int orderNumber;
    private String customerName;
    private String state;
    private BigDecimal taxRate;
    private String productType;
    private BigDecimal area;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;

    public Order(int orderNumber, String customerName, String state,
                 BigDecimal taxRate, String productType, BigDecimal area,
                 BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot,
                 BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax, BigDecimal total) {

        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.state = state;
        this.taxRate = taxRate;
        this.productType = productType;
        this.area = area;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
        this.materialCost = materialCost;
        this.laborCost = laborCost;
        this.tax = tax;
        this.total = total;
    }

    public int getOrderNumber() {
        return orderNumber;
    }


    public String getCustomerName() {
        return customerName;
    }


    public String getState() {
        return state;
    }


    public BigDecimal getTaxRate() {
        return taxRate;
    }


    public String getProductType() {
        return productType;
    }


    public BigDecimal getArea() {
        return area;
    }


    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }


    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }


    public BigDecimal getLaborCost() {
        return laborCost;
    }


    public BigDecimal getTax() {
        return tax;
    }


    public BigDecimal getTotal() {
        return total;
    }

}
