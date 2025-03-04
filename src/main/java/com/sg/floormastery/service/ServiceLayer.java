package com.sg.floormastery.service;

import com.sg.floormastery.dto.Order;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceLayer {

    BigDecimal calMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);
    BigDecimal calLaborCost(BigDecimal area, BigDecimal labelPerSquareFoot);
    BigDecimal calTax (BigDecimal materialCost, BigDecimal labeCost, BigDecimal TaxRate);
    BigDecimal calTotal(BigDecimal materialCost, BigDecimal labeCost, BigDecimal Tax);
    List<Order> getOrders(String date);
}
