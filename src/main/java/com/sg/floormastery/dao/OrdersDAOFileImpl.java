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

    // Key is the order number and the value is the order itself
    private HashMap<Integer, Order> storage = new HashMap<>();
    public String ORDER_FILE_PATH = "Files/Orders/";
    final static String DELIMITER = ",";
    public int nextOrderNumber = 0;


    public OrdersDAOFileImpl() {
        loadAllOrders();
    }
    public OrdersDAOFileImpl(String filePath){
        ORDER_FILE_PATH = filePath;
    }

    //**** MAIN METHODS START****//
    @Override
    public Order addOrder(Order order, String date)throws PersistanceException {
        // Get the file
        String[] fields = date.split("-");
        String filePath = ORDER_FILE_PATH + "Orders_"+fields[0] + fields[1] + fields[2] + ".txt";

        // Create/Overwrite file with the order
        File orderFile = new File(filePath);
        addOrderToFile(orderFile, order);

        // Store order in memory
        storage.put(order.getOrderNumber(), order);
        nextOrderNumber = order.getOrderNumber()+1;
        return order;
    }

    @Override
    public Order editOrder(Order newOrder, String date) throws PersistanceException {
        // Finding and creating the file
        String[] fields = date.split("-");
        String filePath = ORDER_FILE_PATH + "Orders_" + fields[0] + fields[1] + fields[2] + ".txt";
        File orderFile = new File(filePath);

        // Load all orders from the selected file
        List<Order> orders = importFromFiles(filePath); // Load all orders

        // Replace the old order with the new one
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderNumber() == newOrder.getOrderNumber()) {
                orders.set(i, newOrder);
                break;
            }
        }

        editOrderFromFile(orderFile, orders);

        // Update in-memory storage
        storage.put(newOrder.getOrderNumber(), newOrder);
        return newOrder;
    }

    @Override
    public Order removeOrder(Order orderToRemove, String date) throws PersistanceException {
        // Finding and creating the file
        String[] fields = date.split("-");
        String filePath = ORDER_FILE_PATH + "Orders_" + fields[0] + fields[1] + fields[2] + ".txt";
        File orderFile = new File(filePath);

        // Load all orders from the selected file
        List<Order> orders = importFromFiles(filePath);

        Order removedOrder = null;

        // Create a new list without the removed order
        List<Order> updatedOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getOrderNumber() == orderToRemove.getOrderNumber()) {
                removedOrder = o;
            } else {
                updatedOrders.add(o);
            }
        }

        removeOrderInFile(orderFile, updatedOrders);

        // Remove from in-memory storage
        storage.remove(orderToRemove.getOrderNumber());

        return removedOrder;
    }

    @Override
    public void exportOrdersDataToFile(String file) throws PersistanceException {
        try {
            // Attempt to open the file to overwrite on it
            PrintWriter out = new PrintWriter(new FileWriter(file));

            out.println("[ORDERS]");
            out.flush();

            // Using lambdas and streams in DAO
            storage.values().stream()
                    .map(order -> formatOrderForFile(order)) // Convert each order to a string
                    .forEach(line -> out.println(line) ); // Explicit lambda notation

            // Blank line for separation
            out.println();
            out.flush();
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Could not export orders data.");
        }
    }
    //**** MAIN METHODS END ****//

    //**** METHODS WITH FILES START****//
    private void addOrderToFile(File file, Order order)throws PersistanceException{
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file, true));
            // Append order details to file
            out.println(formatOrderForFile(order));
            out.flush();
        }
        catch (IOException e ) {
            throw new PersistanceException("ERROR: Problem reading the orders file");
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

    private void editOrderFromFile(File file, List<Order> orders) throws PersistanceException{
        // Write the updated list back to the file
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            for (Order order : orders) {
                out.println(formatOrderForFile(order)); // Rewrite each order
                out.flush();
            }
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Problem writing to the orders file");
        }
    }

    private void removeOrderInFile(File file, List<Order> updatedOrders) throws PersistanceException{
        // Rewrite the file without the order to remove
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            for (Order o : updatedOrders) {
                out.println(formatOrderForFile(o));
                out.flush();
            }
        } catch (IOException e) {
            throw new PersistanceException("ERROR: Problem writing to the orders file");
        }
    }

    public List<Order> importFromFiles(String file)  throws PersistanceException{
        List<Order> orders = new ArrayList<>();
        try{
            Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
            while(sc.hasNextLine()){
                // Get the line and its fields
                String s = sc.nextLine();
                String[] fields = s.split(DELIMITER);

                // Get all data needed and create the order object
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

                // Add order to the memory storage
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

    public void loadAllOrders() {
        File folder = new File(ORDER_FILE_PATH);

        // Using lambda and listFiles to get all the files in the folder.
        // Then check if the names of each file matches the condition.
        // Dir is the folder directory where the files are
        File[] listOfFiles = folder.
                listFiles((dir, name) -> name.startsWith("Orders_") && name.endsWith(".txt"));


        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                // Using absolute path to convert the file to a string
                List<Order> orders = importFromFiles(file.getAbsolutePath());
                for(Order order : orders){
                    nextOrderNumber = Math.max(order.getOrderNumber()+1, nextOrderNumber);
                    storage.put(order.getOrderNumber(), order);
                }
            }
        }

    }

    //**** METHODS WITH FILES END**** //

    //**** HELPER METHODS START ****//

    @Override
    public Order getOrder(Integer number) {
        return storage.get(number);
    }

    @Override
    public List<Order> getOrdersByDate(String date)  throws PersistanceException{
        // Date should be YY-MM-DD format
        String[] fields = date.split("-");

        String file = ORDER_FILE_PATH+"Orders_"+fields[0]+fields[1]+fields[2]+".txt";
        return importFromFiles(file);
    }

    @Override
    public int getNextOrderNumber() {
        return nextOrderNumber;
    }

    //**** HELPER METHODS END ****//




}
