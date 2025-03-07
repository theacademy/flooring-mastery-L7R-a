package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Tax;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAOStubImpl implements OrdersDAO {
    public final String DELIMITER = ",";
    public Order order;
    public OrdersDAOStubImpl() {
         order = new Order(1,
                "TEST_EDITING", "CA",  new BigDecimal("3.2"), "Woods", new BigDecimal("17.02"),
                new BigDecimal("1.15"), new BigDecimal("2.10"),
                new BigDecimal("70"), new BigDecimal("27"),
                new BigDecimal("100"),new BigDecimal("4500.18"));
    }

    @Override
    public Order getOrder(Integer number) {
        if(number == order.getOrderNumber()){
            return order;
        }
        else{
            return null;
        }
    }

    @Override
    public Order addOrder(Order order, String date) throws PersistanceException {
        return order;
    }

    @Override
    public Order editOrder(Order newOrder, String date) throws PersistanceException {
        return newOrder;
    }

    @Override
    public Order removeOrder(Order orderToRemove, String date) throws PersistanceException {
        return orderToRemove;
    }

    @Override
    public void exportOrdersDataToFile(String file) throws PersistanceException {
        try {
            // Attempt to open the file to overwrite on it
            PrintWriter out = new PrintWriter(new FileWriter(file));

            out.println("[ORDERS]");
            out.flush();

            out.println(formatOrderForFile(order));
            out.println();
            out.flush();
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export orders data.");
        }
    }

    private String formatOrderForFile(Order order) {
        return order.getOrderNumber() + DELIMITER +
                order.getCustomerName() + DELIMITER +
                order.getState() + DELIMITER +
                order.getTaxRate() + DELIMITER +
                order.getProductType() + DELIMITER +
                order.getArea() + DELIMITER +
                order.getCostPerSquareFoot() + DELIMITER +
                order.getLaborCostPerSquareFoot() + DELIMITER +
                order.getMaterialCost() + DELIMITER +
                order.getLaborCost() + DELIMITER +
                order.getTax() + DELIMITER +
                order.getTotal();
    }


    @Override
    public List<Order> getOrdersByDate(String date) {
        List<Order> list = new ArrayList<>();
        list.add(order);
        return list;
    }

    @Override
    public int getNextOrderNumber() {
        return 2;
    }
}
