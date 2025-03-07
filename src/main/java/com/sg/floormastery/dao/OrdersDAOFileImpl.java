package com.sg.floormastery.dao;

import com.sg.floormastery.dto.Order;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Component
public class OrdersDAOFileImpl implements OrdersDAO{
    // Key is the object number and the value is the order itself
    private HashMap<Integer, Order> storage = new HashMap<>();
    public String ORDER_FILE_PATH = "Files/Orders/";
    final static String DELIMITER = ",";
    public int currentSize = 0;


    public OrdersDAOFileImpl() {
        loadAllOrders();
    }

    private void loadAllOrders() {
        File folder = new File(ORDER_FILE_PATH);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.startsWith("Orders_") && name.endsWith(".txt"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                List<Order> orders = importFromFiles(file.getAbsolutePath());
                for(Order order : orders){
                    storage.put(order.getOrderNumber(), order);
                }
            }
        }

        currentSize = storage.size();
    }

    @Override
    public Order getOrder(Integer number) {
        return storage.get(number);
    }

    @Override
    public Order addOrder(Order order, String date) {
        String[] fields = date.split("-");
        String filePath = ORDER_FILE_PATH + "Orders_"+fields[0] + fields[1] + fields[2] + ".txt";

        File orderFile = new File(filePath);
        try {
            PrintWriter out = new PrintWriter(new FileWriter(orderFile, true));
            // Append order details to file
            out.println(formatOrderForFile(order));
            out.flush();
            storage.put(order.getOrderNumber(), order);
        }
        catch (IOException e ) {
                throw new PersistanceException("ERROR: Problem reading the orders file");
            }

        return order;
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
    public Order editOrder(Order newOrder, String date) throws PersistanceException {
        String[] fields = date.split("-");
        String filePath = ORDER_FILE_PATH + "Orders_" + fields[0] + fields[1] + fields[2] + ".txt";

        File orderFile = new File(filePath);
        List<Order> orders = importFromFiles(filePath); // Load all orders

        boolean orderNotFound = orders.stream().noneMatch(order -> order.getOrderNumber() == newOrder.getOrderNumber());
        if(orderNotFound){
            throw new PersistanceException("ERROR: Order number " + newOrder.getOrderNumber() + " not found for date " + date);
        }

        // Replace the old order with the new one
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNumber() == newOrder.getOrderNumber()) {
                orders.set(i, newOrder);
                break;
            }
        }

        // Write the updated list back to the file
        try (PrintWriter out = new PrintWriter(new FileWriter(orderFile))) {
            for (Order order : orders) {
                out.println(formatOrderForFile(order)); // Rewrite each order
            }
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Problem writing to the orders file");
        }

        storage.put(newOrder.getOrderNumber(), newOrder); // Update in-memory storage
        return newOrder;
    }

    @Override
    public Order removeOrder(Order orderToRemove, String date) throws PersistanceException {
        String[] fields = date.split("-");
        String filePath = ORDER_FILE_PATH + "Orders_" + fields[0] + fields[1] + fields[2] + ".txt";

        File orderFile = new File(filePath);
        List<Order> orders = importFromFiles(filePath); // Load all orders from file

        boolean orderFound = false;
        Order removedOrder = null;

        // Create a new list without the removed order
        List<Order> updatedOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getOrderNumber() == orderToRemove.getOrderNumber()) {
                removedOrder = o;
                orderFound = true;
            } else {
                updatedOrders.add(o);
            }
        }

        if (!orderFound) {
            throw new PersistanceException("ERROR: Order number " + orderToRemove.getOrderNumber() + " not found for date " + date);
        }

        // Rewrite the file with the updated orders
        try (PrintWriter out = new PrintWriter(new FileWriter(orderFile))) {
            for (Order o : updatedOrders) {
                out.println(formatOrderForFile(o));
            }
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Problem writing to the orders file");
        }

        // Remove from in-memory storage
        storage.remove(orderToRemove.getOrderNumber());

        return removedOrder; // Return the removed order
    }

    @Override
    public List<Order> getOrdersByDate(String date)  throws PersistanceException{
        // Date should be YY-MM-DD format
        String[] fields = date.split("-");

        String file = ORDER_FILE_PATH+"Orders_"+fields[0]+fields[1]+fields[2]+".txt";
        return importFromFiles(file);
    }

    @Override
    public int getCurrentNumberOfOrders() {
        return storage.size();
    }

    public List<Order> importFromFiles(String file)  throws PersistanceException{
        List<Order> orders = new ArrayList<>();
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
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
                orders.add(order);
            }
        }
        catch (FileNotFoundException e){
            throw new PersistanceException("ERROR: There is no file with this date.");
        }
        catch (IOException | NumberFormatException e ) {
            throw new PersistanceException("ERROR: Problem reading the orders file");
        }
        return orders;
    }

    public void exportOrdersDataToFile(String file) throws PersistanceException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.println("[ORDERS]"); // Section title
            for (Order order : storage.values()) {
                out.println(formatOrderForFile(order));
            }
            out.println(); // Blank line for separation
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export orders data.");
        }
    }
}
