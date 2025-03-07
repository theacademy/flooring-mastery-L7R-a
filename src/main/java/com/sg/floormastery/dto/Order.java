package com.sg.floormastery.dto;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber &&
                Objects.equals(customerName, order.customerName) &&
                Objects.equals(state, order.state) &&
                Objects.equals(taxRate, order.taxRate) &&
                Objects.equals(productType, order.productType) &&
                Objects.equals(area, order.area) &&
                Objects.equals(costPerSquareFoot,
                        order.costPerSquareFoot) &&
                Objects.equals(laborCostPerSquareFoot,
                        order.laborCostPerSquareFoot)
                && Objects.equals(materialCost,
                order.materialCost) &&
                Objects.equals(laborCost, order.laborCost) &&
                Objects.equals(tax, order.tax) &&
                Objects.equals(total, order.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, state,
                taxRate, productType, area, costPerSquareFoot,
                laborCostPerSquareFoot, materialCost, laborCost,
                tax, total);
    }

}
