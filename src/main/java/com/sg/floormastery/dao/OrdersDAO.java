package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;
import java.util.List;

public interface OrdersDAO {

    Order getOrder(Integer number);
    Order addOrder(Order order, String date) throws PersistanceException;
    Order editOrder(Order newOrder, String date) throws PersistanceException;
    Order removeOrder(Order orderToRemove, String date) throws PersistanceException ;
    void exportOrdersDataToFile(String file) throws PersistanceException;

    List<Order> getOrdersByDate(String date);
    int getCurrentNumberOfOrders();
}
