package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class OrdersDAOFileImpl implements OrdersDAO{
    public String ORDER_FILE_PATH = "Files/Orders/Orders_";
    final static String DELIMITER = ",";
    @Override
    public Order getOrder(Integer number) {
        return null;
    }

    @Override
    public Order addOrder(Order order) {
        return null;
    }

    @Override
    public Order editOrder(Integer oldOrderNumber, Object newOrder) {
        return null;
    }

    @Override
    public Order removeOrder(Integer orderNumber) {
        return null;
    }

    @Override
    public List<Order> getOrderByDate(String date) {
        // Date should be YY-MM-DD format
        String[] fields = date.split("-");

        String file = ORDER_FILE_PATH+fields[0]+fields[1]+fields[2]+".txt";
        return importFromFile(file);
    }

    public List<Order> importFromFile(String file) {
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
            storage.clear();
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String[] fields = s.split(DELIMITER);

                Integer orderNumber = Integer.valueOf(fields[0]);
                String customerName = fields[1];
                String state = fields[2];
                BigDecimal taxRate = new BigDecimal(fields[3]);
                String productType = fields[4];
                BigDecimal area = new BigDecimal(fields[5]);
                BigDecimal costPerSquareFoot = new BigDecimal(fields[6]);
                BigDecimal laborCostPerSquareFoot = new BigDecimal(fields[7]);
                BigDecimal materialCost = new BigDecimal(fields[8]);
                BigDecimal laborCost = new BigDecimal(fields[9]);
                BigDecimal tax = new BigDecimal(fields[10]);
                BigDecimal total = new BigDecimal(fields[11]);

                Order order = new Order(orderNumber, customerName, state,
                        taxRate, productType, area, costPerSquareFoot,
                        laborCostPerSquareFoot, materialCost,
                        laborCost, tax, total
                );

                storage.put(order.getOrderNumber(), order);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return new ArrayList<>(storage.values());
    }
}
