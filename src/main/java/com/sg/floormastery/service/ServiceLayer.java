package com.sg.floormastery.service;

import com.sg.floormastery.dao.PersistanceException;
import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

public interface ServiceLayer {

    BigDecimal calMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);
    BigDecimal calLaborCost(BigDecimal area, BigDecimal laborPerSquareFoot);
    BigDecimal calTax (BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);
    BigDecimal calTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
    List<Order> getOrders(String date);
    List<Product> getProducts();
    Tax getTax(String stateCode);
    Order addOrder(Order order, String date);
    boolean isDateValid(String userInput) throws InvalidOrderException;
    public boolean isExistingDateValid(String userInput)  throws InvalidOrderException;
    boolean isNameValid(String userInput) throws InvalidOrderException;
    boolean isStateValid(String userInput) throws InvalidOrderException;
    boolean isProductValid(String userInput) throws InvalidOrderException;
    boolean isAreaValid(String userInput) throws InvalidOrderException;
    boolean isOrderNumberValid(String temporaryNumber)throws InvalidOrderException;
    int getCurrentNumberOfOrders();

    Product getProduct(String productType);

    Order getOrder(Integer orderNumber);

    Order editOrder(Order order);
    Order removeOrder(Order order);
}

