package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface OrdersDAO {
    // Key is the object number and the value is the order itself
    HashMap<Integer, Order> storage = new HashMap<>();

    Order getOrder(Integer number);
    Order addOrder(Order order, String date);
    Order editOrder(Order newOrder, String date);
    Order removeOrder(Order orderToRemove, String date) ;
    List<Order> getOrderByDate(String date);
    int getCurrentNumberOfOrders();
    void exportOrdersDataToFile(String file);
}
