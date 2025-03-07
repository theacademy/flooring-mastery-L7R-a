package com.sg.floormastery.service;

import com.sg.floormastery.dao.PersistanceException;
import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface ServiceLayer {
    //**** MAIN METHODS START ****//
    Order addOrder(Order order, String date);
    Order editOrder(Order order, String date);
    Order removeOrder(Order order, String date);
    void exportAllData();
    //**** MAIN METHODS END ****//

    //**** CALCULATIONS START ****//
    BigDecimal calMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);
    BigDecimal calLaborCost(BigDecimal area, BigDecimal laborPerSquareFoot);
    BigDecimal calTax (BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);
    BigDecimal calTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
    Map<String, BigDecimal> doAllOrderCalculations(Tax tax, Product product, BigDecimal area);
    //**** CALCULATIONS END ****//

    //**** GET METHODS START ****//
    List<Order> getOrders(String date);
    List<Product> getProducts();
    Tax getTax(String stateCode);
    Product getProduct(String productType);
    Order getOrder(Integer orderNumber);
    int getCurrentNumberOfOrders();
    //**** GET METHODS END ****//

    //**** VALIDATIONS START ****//
    boolean isFutureDateValid(String userInput) throws InvalidOrderException;
    boolean isDateValid(String userInput)  throws InvalidOrderException;
    boolean isNameValid(String userInput) throws InvalidOrderException;
    boolean isStateValid(String userInput) throws InvalidOrderException;
    boolean isProductValid(String userInput) throws InvalidOrderException;
    boolean isAreaValid(String userInput) throws InvalidOrderException;
    boolean isOrderNumberValid(String temporaryNumber)throws InvalidOrderException;
    //**** VALIDATIONS END ****//




}

