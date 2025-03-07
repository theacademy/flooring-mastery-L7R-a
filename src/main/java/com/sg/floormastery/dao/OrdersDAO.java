package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;
import java.util.List;

public interface OrdersDAO {

    Order getOrder(Integer number);
    Order addOrder(Order order, String date);
    Order editOrder(Order newOrder, String date);
    Order removeOrder(Order orderToRemove, String date) ;
    void exportOrdersDataToFile(String file);

    List<Order> getOrdersByDate(String date);
    int getCurrentNumberOfOrders();
}
