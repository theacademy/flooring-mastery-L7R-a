package com.sg.floormastery.service;

import com.sg.floormastery.dao.OrdersDAO;
import com.sg.floormastery.dao.PersistanceException;
import com.sg.floormastery.dao.ProductsDAO;
import com.sg.floormastery.dao.TaxDAO;
import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

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

    //**** MAIN METHODS START ****//

    @Override
    public Order addOrder(Order order, String date) throws PersistanceException {
        return orders.addOrder(order, date);
    }

    @Override
    public Order editOrder(Order order, String date)throws PersistanceException {
        return orders.editOrder(order, date);
    }

    @Override
    public Order removeOrder(Order order, String date)throws PersistanceException {
        return orders.removeOrder(order, date);
    }

    @Override
    public void exportAllData() {
        // All components will be writing & appending to the same file in order
        final String EXPORT_FILE_PATH = "Files/Backup/ExportedData.txt";
        orders.exportOrdersDataToFile(EXPORT_FILE_PATH);
        products.exportProductsDataToFile(EXPORT_FILE_PATH);
        taxes.exportTaxesDataToFile(EXPORT_FILE_PATH);
    }

    //**** MAIN METHODS END ****//

    //**** CALCULATIONS START ****//

    @Override
    public BigDecimal calMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calLaborCost(BigDecimal area, BigDecimal labelPerSquareFoot) {
        return area.multiply(labelPerSquareFoot).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        return materialCost.add(laborCost).multiply(taxRate).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calTotal(BigDecimal materialCost, BigDecimal labeCost, BigDecimal tax) {
        return materialCost.add(labeCost).add(tax).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Map<String, BigDecimal> doAllOrderCalculations(Tax tax, Product product, BigDecimal area) {
        // Initialize all variables needed
        Map<String, BigDecimal> calculationsData = new HashMap<>();
        BigDecimal costPerSquareFoot, laborCostPerSquareFoot, taxRate, materialCost, laborCost, taxCost,total;

        // Get the values from the tax and products classes
        costPerSquareFoot = product.getCostPerSquareFoot();
        laborCostPerSquareFoot = product.getLaborCostPerSquareFoot();
        taxRate = tax.getTaxRate();

        // Do each calculation and store them accordingly
        materialCost = calMaterialCost(area, costPerSquareFoot);
        laborCost = calLaborCost(area, laborCostPerSquareFoot);
        taxCost = calTax(materialCost, laborCost, taxRate);
        total = calTotal(materialCost, laborCost, taxCost);

        // Add them to the hash map
        calculationsData.put("taxRate",taxRate);
        calculationsData.put("costPerSquareFoot",costPerSquareFoot);
        calculationsData.put("laborCostPerSquareFoot",laborCostPerSquareFoot);
        calculationsData.put("materialCost",materialCost);
        calculationsData.put("laborCost",laborCost);
        calculationsData.put("taxCost",taxCost);
        calculationsData.put("total",total);

        return calculationsData;
    }

    //**** CALCULATIONS END ****//

    //**** GET METHODS START ****//

    @Override
    public Order getOrder(Integer orderNumber) throws  PersistanceException{
        Order order = orders.getOrder(orderNumber);
        if(order == null){
            throw new PersistanceException("ERROR: The order with this number couldn't be found!");
        }
        return order;
    }

    @Override
    public List<Order> getOrders(String date)  throws PersistanceException{
        List<Order> results = orders.getOrdersByDate(date);
        if(results == null){
            throw new PersistanceException("ERROR: The orders with this date couldn't be found!"+
                    " Perhaps user removed the orders in this file!");
        }
        return results;
    }

    @Override
    public List<Product> getProducts() {
        return products.getAllProducts();
    }

    @Override
    public Tax getTax(String stateCode) {
        return taxes.getTax(stateCode.toUpperCase());
    }

    @Override
    public Product getProduct(String productType) {
        // Making sure the input is capitalized properly
        String formattedInput = productType.substring(0, 1).toUpperCase() +
                productType.substring(1).toLowerCase();

        return products.getProduct(formattedInput);
    }

    @Override
    public int getNextOrderNumber() {
        return orders.getNextOrderNumber();
    }

    //**** GET METHODS END ****//

    //**** VALIDATIONS START ****//

    @Override
    public boolean isFutureDateValid(String userInput) throws InvalidOrderException {
        //Get date and format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate today = LocalDate.now();

        try {
            // Verify it can be converted to LocalDate & is in the future
            LocalDate userDate = LocalDate.parse(userInput, formatter);
            if (today.isAfter(userDate) || today.equals(userDate)) {
                throw new InvalidOrderException("ERROR: The date must be set to be in the future");
            }

        } catch (DateTimeParseException e) {
            throw new InvalidOrderException("ERROR: Invalid date format. Please use MM-dd-yyyy.");
        }
        return true;
    }

    @Override
    public boolean isDateValid(String userInput)  throws InvalidOrderException{
        //Get format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        // Verify it can be converted to LocalDate
        try {
            LocalDate.parse(userInput, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidOrderException("ERROR: Invalid date format. Please use MM-dd-yyyy.");
        }
        return true;
    }

    @Override
    public boolean isNameValid(String userInput) throws InvalidOrderException{
        if(userInput.isBlank()){
            throw new InvalidOrderException("ERROR: Name cannot be left in blank");
        }

        // If the input has anything that is not a character, number, period, or comma, throw error
        Pattern p = Pattern.compile("[^a-zA-Z0-9., ]");
        if(p.matcher(userInput).find()){
            throw new InvalidOrderException("ERROR: Name can only have character, numbers, periods, and commas");
        }
        return true;
    }

    @Override
    public boolean isStateValid(String userInput) throws InvalidOrderException {
        // Check if the input is inside the taxes dao memory
        boolean exists = taxes.getStatesAbbreviation().stream()
                .anyMatch(state -> state.equalsIgnoreCase(userInput));

        if (exists) {
            return true;
        } else {
            throw new InvalidOrderException("ERROR: Unable to sell to the selected state. Available states are: " +
                    taxes.getStatesAbbreviation());
        }
    }

    @Override
    public boolean isProductValid(String userInput) throws InvalidOrderException{
        // Check if the input is inside the taxes dao memory
        boolean exists = products.getAllProductTypes().stream()
                .anyMatch(product -> product.equalsIgnoreCase(userInput));

        if (exists) {
            return true;
        } else {
            throw new InvalidOrderException("ERROR: Product not found. Available products are: "  +
                    products.getAllProductTypes());
        }
    }

    @Override
    public boolean isAreaValid(String userInput) throws InvalidOrderException{
        // Check it can be converted into a decimal number & is greater than 100
        try {
            double num = Double.parseDouble(userInput);
            if(num < 100){
                throw new InvalidOrderException("ERROR: Number must be at least 100 sq ft.");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidOrderException("ERROR: Please enter a positive number");
        }
    }

    @Override
    public boolean isOrderNumberValid(String temporaryNumber) throws InvalidOrderException{
        // Check it can be converted into an Integer number
        try {
            Integer.parseInt(temporaryNumber);
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidOrderException("ERROR: Please enter a positive number");
        }
    }

    //**** VALIDATIONS END ****//

}
