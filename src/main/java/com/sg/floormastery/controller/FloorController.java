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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FloorController {

    private FloorView view;
    private ServiceLayer service;

    @Autowired
    public FloorController(FloorView view, ServiceLayer service) {
        this.view = view;
        this.service = service;
    }

    // **** SELECT ACTION METHODS START **** //

    // Execute one of the main CRUD methods using output from getMenuOption
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

    // Get user menu input as an int
    private int getMenuOption(){
        try{
            return view.displayMenu();
        }
        catch (InvalidOrderException e){
            view.displayErrorMessage(e.getMessage());
            view.displayPressEnterToContinue();
            return -1;
        }
    }

    // **** SELECT ACTION METHODS END **** //


    // **** MAIN METHODS START **** //

    private void displayOrders(){
        view.displayOrdersBanner();

        // getOrders gives the orders to display as a hashMap with only 1 key (date).
        Map<String, List<Order>> dateOrdersMap = getOrders();
        String date = dateOrdersMap.keySet().iterator().next();
        List<Order> userOrders = dateOrdersMap.get(date);

        view.displayOrders(userOrders);
    }

    private void addOrder() {
        view.displayAddOrderBanner();

        // All products available
        List<Product> products = service.getProducts();

        // Variables that take user input values
        String date, name, state, product, area;
        date = name = state = product = area = null;

        boolean hasErrors = false;


        do {
            /* In each if statement, first store any user input.
            * Then check if the service validates it.
            * If yes, then store it in the final variable, else it is an error */
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
                    view.displayProducts(products);
                    String temporaryProduct = view.getUserProductType();
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

        if(performAction.equalsIgnoreCase("Y")){
            Order result = service.addOrder(order, date);
            view.displayActionResult(result, "added");
        }
    }

    private void editOrder(){
        view.displayEditOrderBanner();
        Integer orderNumber = null;
        Map<String, List<Order>> dateOrdersMap = getOrders();
        if(dateOrdersMap.isEmpty()){
            return;
        }

        String date = dateOrdersMap.keySet().iterator().next();
        List<Order> orders = dateOrdersMap.get(date);


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
            performAction = view.confirmAction("edit");
        }while (!performAction.equalsIgnoreCase("Y") && !performAction.equalsIgnoreCase("N"));

        String newName, newState, newProductType, newArea;
        newName = newState = newProductType = newArea = null;

        boolean hasErrors = false;

        view.displayAddOrderBanner();

        do {
            try {
                if(newName == null){
                    view.displayCurrentName(order);
                    String temporaryName = view.getUserOrderName();
                    if(temporaryName.equals("")){
                        newName = order.getCustomerName();
                    }
                    else{
                        newName = validateInput(temporaryName, service.isNameValid(temporaryName));
                    }
                }
                if(newState == null){
                    view.displayCurrentState(order);
                    String temporaryState = view.getUserOrderState();
                    if(temporaryState.equals("")){
                        newState = order.getState();
                    }
                    else{
                        newState = validateInput(temporaryState, service.isStateValid(temporaryState));
                    }

                }
                if(newProductType == null){
                    view.displayCurrentProductType(order);
                    String temporaryProduct = view.getUserProductType();
                    if(temporaryProduct.equals("")){
                        newProductType = order.getProductType();
                    }
                    else{
                        newProductType = validateInput(temporaryProduct, service.isProductValid(temporaryProduct));
                    }
                }
                if(newArea == null){
                    view.displayCurrentArea(order);
                    String temporaryArea = view.getUserOrderArea();
                    if(temporaryArea.equals("")){
                        newArea = String.valueOf(order.getArea());
                    }
                    else{
                        newArea = validateInput(temporaryArea, service.isAreaValid(temporaryArea));
                    }
                }
                hasErrors = false;
            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        Tax taxSelected = service.getTax(newState);
        Product productSelected = service.getProduct(newProductType);


        BigDecimal areaSelected, costPerSquareFoot, laborCostPerSquareFoot, taxRate, materialCost, laborCost, tax,total;

        areaSelected = new BigDecimal(newArea).setScale(2, RoundingMode.HALF_UP);
        costPerSquareFoot = productSelected.getCostPerSquareFoot();
        laborCostPerSquareFoot = productSelected.getLaborCostPerSquareFoot();
        taxRate = taxSelected.getTaxRate();

        materialCost = service.calMaterialCost(areaSelected, costPerSquareFoot);
        laborCost = service.calLaborCost(areaSelected, laborCostPerSquareFoot);
        tax = service.calTax(materialCost, laborCost, taxRate);
        total = service.calTotal(materialCost, laborCost, tax);

        Order newOrder = new Order(order.getOrderNumber(),
                newName, newState, taxRate, newProductType, areaSelected,
                costPerSquareFoot, laborCostPerSquareFoot,
                materialCost, laborCost, tax, total);


        Order result = service.editOrder(newOrder, date);
        view.displayActionResult(result, "edited");

    }

    private void removeOrder(){
        view.displayRemoveOrderBanner();
        Map<String, List<Order>> dateOrdersMap = getOrders();
        if(dateOrdersMap.isEmpty()){
            return;


        }
        Integer orderNumber = null;
        String date = dateOrdersMap.keySet().iterator().next();
        List<Order> orders = dateOrdersMap.get(date);

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

        Order result = service.removeOrder(order, date);
        view.displayActionResult(result, "removed");
    }

    private void exportAllData(){
        try{
            service.exportAllData();
            view.displaySuccessExportation();
        }
        catch (PersistanceException e){
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void quit(){
        view.displayExitBanner();
        return;
    }

    // **** MAIN METHODS END **** //

    // **** HELPER METHODS START **** //

    private Map<String, List<Order>> getOrders(){

        boolean hasErrors = false;
        String date = null;
        List<Order> orders = null;
        do {
            try {
                String temporaryDate = view.getUserOrderDate();
                date = validateInput(temporaryDate, service.isExistingDateValid(temporaryDate));
                orders = service.getOrders(date);
                if(orders.isEmpty()){
                    throw new PersistanceException("ERROR: There are no orders with this date!");
                }
                hasErrors = false;
            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        Map<String, List<Order>> dateOrdersMap = new HashMap<>();
        dateOrdersMap.put(date, orders);
        return dateOrdersMap;
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

    // Given an input and a validation from the service layer, return the input if it passed the validation
    private String validateInput(String input, boolean validation) throws InvalidOrderException{
        if(validation){
            return input;
        }
        return null;
    }

    // **** HELPER METHODS END **** //
}
