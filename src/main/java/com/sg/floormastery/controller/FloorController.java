package com.sg.floormastery.controller;

import com.sg.floormastery.dao.PersistanceException;
import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import com.sg.floormastery.service.InvalidOrderException;
import com.sg.floormastery.service.ServiceLayer;
import com.sg.floormastery.ui.FloorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class FloorController {

    private FloorView view;
    private ServiceLayer service;

    @Autowired
    public FloorController(FloorView view, ServiceLayer service) {
        this.view = view;
        this.service = service;
    }


    public void run(){
        boolean keep = true;

        while (keep) {
            int option = getMenuOption();
            switch (option) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportAllData();
                    break;
                case 6:
                    quit();
                    keep = false;
                    break;

            }
        }
    }

    private int getMenuOption(){
        return view.displayMenu();
    }
    private void displayOrders(){
        String userDate = view.displayOrdersBanner();
        List<Order> userOrders = service.getOrders(userDate);
        view.displayOrders(userOrders);
    }

    // Given an input and a validation from the service layer, return the input if it passed the validation
    private String validateInput(String input, boolean validation) throws InvalidOrderException{
        if(validation){
            return input;
        }
        return null;
    }

    private void addOrder() {
        // Setting up variables to add the order
        List<String> orderData = new ArrayList<>();
        List<Product> products = service.getProducts();
        String date, name, state, product, area;
        date = name = state = product = area = null;
        boolean hasErrors = false;

        view.displayAddOrderBanner();

        do {
            /* User types their input and is stored in a temporary variable.
             If it passed the service layer validation,
             Then it will be stored in the definitive variable, else it is an error */
            try {
                if(date == null){
                    String temporaryDate = view.getUserOrderDate();
                    date = validateInput(temporaryDate, service.isDateValid(temporaryDate));
                }
                if(name == null){
                    String temporaryName = view.getUserOrderName();
                    name = validateInput(temporaryName, service.isNameValid(temporaryName));
                }
                if(state == null){
                    String temporaryState = view.getUserOrderState();
                    state = validateInput(temporaryState, service.isStateValid(temporaryState));

                }
                if(product == null){
                    String temporaryProduct = view.getUserOrderProduct(products);
                    product = validateInput(temporaryProduct, service.isProductValid(temporaryProduct));

                }
                if(area == null){
                    String temporaryArea = view.getUserOrderArea();
                    area = validateInput(temporaryArea, service.isAreaValid(temporaryArea));

                }
                hasErrors = false;
            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        Tax taxSelected = service.getTax(state);
        Product productSelected = service.getProduct(product);


        BigDecimal areaSelected, costPerSquareFoot, laborCostPerSquareFoot, taxRate, materialCost, laborCost, tax,total;

        areaSelected = new BigDecimal(area).setScale(2, RoundingMode.HALF_UP);
        costPerSquareFoot = productSelected.getCostPerSquareFoot();
        laborCostPerSquareFoot = productSelected.getLaborCostPerSquareFoot();
        taxRate = taxSelected.getTaxRate();

        materialCost = service.calMaterialCost(areaSelected, costPerSquareFoot);
        laborCost = service.calLaborCost(areaSelected, laborCostPerSquareFoot);
        tax = service.calTax(materialCost, laborCost, taxRate);
        total = service.calTotal(materialCost, laborCost, tax);

        Order order = new Order(service.getCurrentNumberOfOrders()+1,
                name, state, taxRate, product, areaSelected,
                costPerSquareFoot, laborCostPerSquareFoot,
                materialCost, laborCost, tax, total);


        view.displayOrderInformation(order);
        String performAction = "";
        do{
            performAction = view.confirmAction("add");
        }while (!performAction.equalsIgnoreCase("Y") && !performAction.equalsIgnoreCase("N"));

        Order result = service.addOrder(order);
        view.displayActionResult(result, "added");
    }

    private void editOrder(){
        Integer orderNumber = null;
        List<Order> orders = getOrdersDate();

        view.displayRemoveOrderBanner();

        // If there are no orders with this date
        if(orders == null){
            view.displayErrorMessage("No order has this date");
            return;
        }

        orderNumber = getOrderNumber();
        Order order = service.getOrder(orderNumber);

        // Date exist but doesn't have this order number
        if(order == null){
            view.displayErrorMessage("No order number matched with the date given");
            return;
        }


        
        view.displayOrderInformation(order);
        String performAction = "";
        do{
            performAction = view.confirmAction("add");
        }while (!performAction.equalsIgnoreCase("Y") && !performAction.equalsIgnoreCase("N"));

        Order result = service.editOrder(order);
        view.displayActionResult(result, "edited");

    }

    private void removeOrder(){
        Integer orderNumber = null;
        List<Order> orders = getOrdersDate();

        view.displayRemoveOrderBanner();

        // If there are no orders with this date
        if(orders == null){
            view.displayErrorMessage("No order has this date");
            return;
        }

        orderNumber = getOrderNumber();
        Order order = service.getOrder(orderNumber);

        // Date exist but doesn't have this order number
        if(order == null){
            view.displayErrorMessage("No order number matched with the date given");
            return;
        }



        view.displayOrderInformation(order);
        String performAction = "";
        do{
            performAction = view.confirmAction("remove");
        }while (!performAction.equalsIgnoreCase("Y") && !performAction.equalsIgnoreCase("N"));

        Order result = service.addOrder(order);
        view.displayActionResult(result, "removed");
    }

    private List<Order> getOrdersDate(){

        boolean hasErrors = false;
        String date = null;
        do {
            try {
                String temporaryDate = view.getUserOrderDate();
                date = validateInput(temporaryDate, service.isDateValid(temporaryDate));
                hasErrors = false;
            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        return service.getOrders(date);
    }

    private Integer getOrderNumber(){
        boolean hasErrors = false;
        Integer number = null;
        String strNumber = null;
        do {
            number = null;
            try {
                strNumber = view.getUserOrderNumber();
                if(service.isOrderNumberValid(strNumber)){
                    number = Integer.parseInt(strNumber);
                }
                hasErrors = false;
            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        return number;
    }

    private void exportAllData(){

    }

    private void quit(){
        return;
    }
}
