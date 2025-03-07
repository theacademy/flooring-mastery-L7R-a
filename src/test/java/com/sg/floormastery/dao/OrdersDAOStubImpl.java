package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;

import java.util.List;

public class OrdersDAOStubImpl implements OrdersDAO {
    @Override
    public Order getOrder(Integer number) {
        return null;
    }

    @Override
    public Order addOrder(Order order, String date) throws PersistanceException {
        return null;
    }

    @Override
    public Order editOrder(Order newOrder, String date) throws PersistanceException {
        return null;
    }

    @Override
    public Order removeOrder(Order orderToRemove, String date) throws PersistanceException {
        return null;
    }

    @Override
    public void exportOrdersDataToFile(String file) throws PersistanceException {

    }

    @Override
    public List<Order> getOrdersByDate(String date) {
        return List.of();
    }

    @Override
    public int getNextOrderNumber() {
        return 0;
    }
}
