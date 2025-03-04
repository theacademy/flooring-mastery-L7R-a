package com.sg.floormastery.service;

import com.sg.floormastery.dao.OrdersDAO;
import com.sg.floormastery.dao.ProductsDAO;
import com.sg.floormastery.dao.TaxDAO;
import com.sg.floormastery.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ServiceLayerImpl implements ServiceLayer{
    private OrdersDAO orders;
    private TaxDAO taxes;
    private ProductsDAO products;

    @Autowired
    public ServiceLayerImpl(OrdersDAO orders, TaxDAO taxes, ProductsDAO products) {
        this.orders = orders;
        this.taxes = taxes;
        this.products = products;
    }

    @Override
    public BigDecimal calMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return null;
    }

    @Override
    public BigDecimal calLaborCost(BigDecimal area, BigDecimal labelPerSquareFoot) {
        return null;
    }

    @Override
    public BigDecimal calTax(BigDecimal materialCost, BigDecimal labeCost, BigDecimal TaxRate) {
        return null;
    }

    @Override
    public BigDecimal calTotal(BigDecimal materialCost, BigDecimal labeCost, BigDecimal Tax) {
        return null;
    }

    @Override
    public List<Order> getOrders(String date) {
        return orders.getOrderByDate(date);
    }


}
