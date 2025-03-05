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
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public List<Order> getOrders(String date) {
        return orders.getOrderByDate(date);
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
        String casing = productType.substring(0, 1).toUpperCase() +
                productType.substring(1).toLowerCase();
        return products.getProduct(casing);
    }

    @Override
    public Order getOrder(Integer orderNumber) {
        return orders.getOrder(orderNumber);
    }

    @Override
    public Order addOrder(Order order) {
       return orders.addOrder(order);
    }


    @Override
    public boolean isDateValid(String userInput) throws InvalidOrderException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate today = LocalDate.now();

        try {
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
        boolean exists = taxes.getStatesAbbreviation().stream()
                .anyMatch(state -> state.equalsIgnoreCase(userInput));
        if (exists) {
            return true;
        } else {
            throw new InvalidOrderException("ERROR: Unable to sell to the selected state. Available states are: " +
                    taxes.getStatesAbbreviation() + ". You can exit this command by typing 'exit'. ");
        }
    }

    @Override
    public boolean isProductValid(String userInput) throws InvalidOrderException{
        boolean exists = products.getAllProductTypes().stream()
                .anyMatch(product -> product.equalsIgnoreCase(userInput));
        if (exists) {
            return true;
        } else {
            throw new InvalidOrderException("ERROR: Product not found. Available products are: "  +
                    products.getAllProductTypes() + ". You can exit this command by typing 'exit'. ");
        }

    }

    @Override
    public boolean isAreaValid(String userInput) throws InvalidOrderException{
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
        try {
            int num = Integer.parseInt(temporaryNumber);
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidOrderException("ERROR: Please enter a positive number");
        }
    }

    @Override
    public int getCurrentNumberOfOrders() {
        return orders.getCurrentNumberOfOrders();
    }


}
