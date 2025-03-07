package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class OrdersDAOFileImplTest {

    OrdersDAOFileImpl testDao;

    @BeforeEach
    public void setUp() throws Exception{
        String testPath = "Files/TestsOrders/";
        // Use the FileWriter to quickly blank the file
        testDao = new OrdersDAOFileImpl(testPath);
    }


    @Test
    void testAddGetOrderAddFile() throws Exception{
        String date = "05-28-2025";
        Order order = new Order(1,
                "Diego", "TX",  new BigDecimal("4.45"), "Tiles", new BigDecimal("127.02"),
                new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("120"), new BigDecimal("83"),
                new BigDecimal("77"),new BigDecimal("3900.12"));
        testDao.addOrder(order, date);

        Order orderStored = testDao.getOrder(1);

        assertEquals(order.getOrderNumber(),
                orderStored.getOrderNumber(),
                "Checking order number");
        assertEquals(order.getCustomerName(),
                orderStored.getCustomerName(),
                "Checking customer name");
        assertEquals(order.getState(),
                orderStored.getState(),
                "Checking order state");
        assertEquals(order.getProductType(),
                orderStored.getProductType(),
                "Checking order product type");
        assertEquals(order.getArea(),
                orderStored.getArea(),
                "Checking order area");

        // Specify the path where the file should be created
        String filePath = "Files/TestsOrders/Orders_05282025.txt";

        // Check if the file was created
        File file = new File(filePath);
        assertTrue(file.exists(), "File should exist after adding an order");

    }

    @Test
    void testAddEditOrder() {
        String date = "05-28-2025";
        Order order = new Order(1,
                "Diego", "TX",  new BigDecimal("4.45"), "Tiles", new BigDecimal("127.02"),
                new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("120"), new BigDecimal("83"),
                new BigDecimal("77"),new BigDecimal("3900.12"));
        testDao.addOrder(order, date);

        Order orderEdited = new Order(1,
                "TEST_EDITING", "CA",  new BigDecimal("3.2"), "Woods", new BigDecimal("17.02"),
                new BigDecimal("1.15"), new BigDecimal("2.10"),
                new BigDecimal("70"), new BigDecimal("27"),
                new BigDecimal("100"),new BigDecimal("4500.18"));

        testDao.editOrder(orderEdited, date);

        Order orderStored = testDao.getOrder(1);

        assertEquals(orderEdited.getOrderNumber(),
                orderStored.getOrderNumber(),
                "Checking order number");
        assertEquals(orderEdited.getCustomerName(),
                orderStored.getCustomerName(),
                "Checking customer name");
        assertEquals(orderEdited.getState(),
                orderStored.getState(),
                "Checking order state");
        assertEquals(orderEdited.getProductType(),
                orderStored.getProductType(),
                "Checking order product type");
        assertEquals(orderEdited.getArea(),
                orderStored.getArea(),
                "Checking order area");
    }

    @Test
    void testAddRemoveOrder() {
        String date = "05-28-2025";
        Order order = new Order(1,
                "Diego", "TX",  new BigDecimal("4.45"),
                "Tiles", new BigDecimal("127.02"),
                new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("120"), new BigDecimal("83"),
                new BigDecimal("77"),new BigDecimal("3900.12"));
        testDao.addOrder(order, date);


        Order removed = testDao.removeOrder(order, date);

        assertNull(testDao.getOrder(removed.getOrderNumber()),
                "Order should be removed and not found");

    }

    @Test
    void testAddGetOrdersByDateGetNextOrderNumber() {
        String date = "05-28-2025";
        Order order = new Order(1,
                "Diego", "TX",  new BigDecimal("4.45"),
                "Tiles", new BigDecimal("127.02"),
                new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("120"), new BigDecimal("83"),
                new BigDecimal("77"),new BigDecimal("3900.12"));
        testDao.addOrder(order, date);

        Order secondOrder = new Order(2,
                "TEST_EDITING", "CA",  new BigDecimal("3.2"), "Woods", new BigDecimal("17.02"),
                new BigDecimal("1.15"), new BigDecimal("2.10"),
                new BigDecimal("70"), new BigDecimal("27"),
                new BigDecimal("100"),new BigDecimal("4500.18"));
        testDao.addOrder(secondOrder, date);

        List<Order> orders = testDao.getOrdersByDate(date);
        int next = testDao.getNextOrderNumber();

        Order orderStored = orders.get(1);
        Order orderStoredSecond = orders.get(0);

        assertEquals(order.getOrderNumber(),
                orderStored.getOrderNumber(),
                "Checking order number");
        assertEquals(order.getCustomerName(),
                orderStored.getCustomerName(),
                "Checking customer name");
        assertEquals(order.getState(),
                orderStored.getState(),
                "Checking order state");
        assertEquals(order.getProductType(),
                orderStored.getProductType(),
                "Checking order product type");
        assertEquals(order.getArea(),
                orderStored.getArea(),
                "Checking order area");

        assertEquals(secondOrder.getOrderNumber(),
                orderStoredSecond.getOrderNumber(),
                "Checking second order number");
        assertEquals(secondOrder.getCustomerName(),
                orderStoredSecond.getCustomerName(),
                "Checking second customer name");
        assertEquals(secondOrder.getState(),
                orderStoredSecond.getState(),
                "Checking second order state");
        assertEquals(secondOrder.getProductType(),
                orderStoredSecond.getProductType(),
                "Checking second order product type");
        assertEquals(secondOrder.getArea(),
                orderStoredSecond.getArea(),
                "Checking second order area");

        assertEquals(3, next,
                "Next order should be number 3");

    }


}