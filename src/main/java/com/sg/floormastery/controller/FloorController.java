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

    public void run(){
        boolean keep = true;
        while (keep) {
        try {
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
        } catch (PersistanceException | InvalidOrderException e) {
            view.displayErrorMessage(e.getMessage());
            view.displayPressEnterToContinue();
        }
        }
    }

    private int getMenuOption() throws InvalidOrderException{
        return view.displayMenu();

    }

    // **** SELECT ACTION METHODS END **** //


    // **** MAIN METHODS START **** //

    private void displayOrders(){
        view.displayOrdersBanner();

        // Get an existing date and then all orders with it
        String date = getOrderDate();
        List<Order> userOrders = service.getOrders(date);

        view.displayOrders(userOrders);
    }

    private void addOrder() throws PersistanceException {
        view.displayAddOrderBanner();

        // All products available
        List<Product> products = service.getProducts();

        // Variables that take user input values
        String date, name, state, product, area;
        date = name = state = product = area = null;

        boolean hasErrors = false;


        /* In each if statement, first store any user input.
        * Then check if the service validates it.
        * If yes, then store it in the final variable,
        * else it is an error and user tries again */
        do {
            try {

                if(date == null){
                    String temporaryDate = view.getUserOrderDate();
                    date = service.isFutureDateValid(temporaryDate) ? temporaryDate : null;
                }

                if(name == null){
                    String temporaryName = view.getUserOrderName();
                    name = service.isNameValid(temporaryName) ? temporaryName : null;
                }

                if(state == null){
                    String temporaryState = view.getUserOrderState();
                    state = service.isStateValid(temporaryState) ? temporaryState : null;
                }

                if(product == null){
                    view.displayProducts(products);
                    String temporaryProduct = view.getUserProductType();
                    product = service.isProductValid(temporaryProduct) ? temporaryProduct : null;

                }

                if(area == null){
                    String temporaryArea = view.getUserOrderArea();
                    area = service.isAreaValid(temporaryArea) ? temporaryArea : null;
                }

                hasErrors = false;

            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        Order order = createOrder(name, service.getCurrentNumberOfOrders()+1, state, product, area);

        view.displayOrderInformation(order);

        // Confirm the user wants to do the action and do it if so
        String performAction = confirmAction("add");

        if(performAction.equalsIgnoreCase("Y")){
            Order result = service.addOrder(order, date);
            view.displayActionResult(result, "added");
        }

    }

    private void editOrder() throws PersistanceException{
        view.displayEditOrderBanner();

        boolean hasErrors = false;

        // Initializing variables that could be updated
        String newName, newState, newProductType, newArea;
        newName = newState = newProductType = newArea = null;

        // Variables used to find the order to update
        Integer orderNumber;
        String date;

        // Get an existing order with the user order number and date
         date = getOrderDate();
        orderNumber = getValidOrderNumber();
        Order order = service.getOrder(orderNumber);

        // Show the order found and ask if they want to edit it
        view.displayOrderInformation(order);
        String performAction = confirmAction("edit");
        if(performAction.equalsIgnoreCase("N")){
           return;
        }

        // Start the editing process of this order
        view.displayStartEditingOrder();

        /* For each field, show current data, save user input in temporary data,
            If input changed and is valid, and store the changes.
            If is blank, keep the current values. Otherwise, error and user tries again.
        */
        do {
            try {
                if(newName == null){
                    view.displayCurrentName(order);
                    String temporaryName = view.getUserOrderName();
                    if(temporaryName.isBlank()){
                        newName = order.getCustomerName();
                    }
                    else{
                        newName = service.isNameValid(temporaryName) ? temporaryName : null;
                    }
                }
                if(newState == null){
                    view.displayCurrentState(order);
                    String temporaryState = view.getUserOrderState();
                    if(temporaryState.isBlank()){
                        newState = order.getState();
                    }
                    else{
                        newState = service.isStateValid(temporaryState) ? temporaryState : null;
                    }

                }
                if(newProductType == null){
                    view.displayCurrentProductType(order);
                    String temporaryProduct = view.getUserProductType();
                    if(temporaryProduct.equals("")){
                        newProductType = order.getProductType();
                    }
                    else{
                        newProductType = service.isProductValid(temporaryProduct) ? temporaryProduct : null;
                    }
                }
                if(newArea == null){
                    view.displayCurrentArea(order);
                    String temporaryArea = view.getUserOrderArea();
                    if(temporaryArea.equals("")){
                        newArea = String.valueOf(order.getArea());
                    }
                    else{
                        newArea = service.isAreaValid(temporaryArea) ? temporaryArea : null;
                    }
                }
                hasErrors = false;
            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

        // Calculate and create new order with this information
        Order newOrder = createOrder(newName, orderNumber, newState, newProductType, newArea);

        view.displayOrderInformation(newOrder);

        // Confirm if they want to save changes
        performAction = confirmAction("save");
        if(performAction.equalsIgnoreCase("N")){
            return;
        }

        // Put the update both in memory and in file.
        Order result = service.editOrder(newOrder, date);
        view.displayActionResult(result, "edited");

    }

    private void removeOrder() throws PersistanceException{
        view.displayRemoveOrderBanner();

        // Variables used to find the order to update
        Integer orderNumber;
        String date;

        // Get an existing order with the user order number and date
        date = getOrderDate();
        orderNumber = getValidOrderNumber();
        Order order = service.getOrder(orderNumber);

        // Show order and confirm they want to remove it
        view.displayOrderInformation(order);
        String performAction = confirmAction("remove");
        if(performAction.equalsIgnoreCase("N")){
            return;
        }

        // Put the remove both in memory and in file.
        Order result = service.removeOrder(order, date);
        view.displayActionResult(result, "removed");
    }

    private void exportAllData() throws PersistanceException{
        service.exportAllData();
        view.displaySuccessExportation();
    }

    private void quit(){
        view.displayExitBanner();
        return;
    }

    // **** MAIN METHODS END **** //

    // **** HELPER METHODS START **** //
    private String confirmAction(String action){
        String performAction = "";
        do{
            performAction = view.confirmAction(action);
        }while (!performAction.equalsIgnoreCase("Y") && !performAction.equalsIgnoreCase("N"));

        return performAction;
    }

    private Order createOrder(String name, Integer number, String state, String product, String area){
        // Create objects to get the details of the calculations
        Tax taxSelected = service.getTax(state);
        Product productSelected = service.getProduct(product);
        BigDecimal areaSelected = new BigDecimal(area).setScale(2, RoundingMode.HALF_UP);

        // Do the calculations to create an object. Keys are the field name and the values are the BigDecimals
        Map<String, BigDecimal>  costs = service.doAllOrderCalculations(taxSelected, productSelected, areaSelected);

        return new Order(number,
                name, state, costs.get("taxRate"), product, areaSelected,
                costs.get("costPerSquareFoot"), costs.get("laborCostPerSquareFoot"),
                costs.get("materialCost"), costs.get("laborCost"),
                costs.get("taxCost"), costs.get("total"));
    }

    private String getOrderDate(){

        boolean hasErrors = false;
        String date = null;
        List<Order> orders = null;
        do {
            try {
                // Get input, verify it is valid and not empty. Otherwise error
                String temporaryDate = view.getUserOrderDate();
                date = service.isDateValid(temporaryDate) ? temporaryDate : null;
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

        return date;
    }

    private Integer getValidOrderNumber(){
        boolean hasErrors = false;
        Integer number = null;
        String strNumber = null;
        do {
            try {
                // Get input, verify it is valid and that exists. Otherwise error
                strNumber = view.getUserOrderNumber();
                if(service.isOrderNumberValid(strNumber)){
                    number = Integer.parseInt(strNumber);
                }
                if(service.getOrder(number) == null){
                    throw new InvalidOrderException("Order with this number couldn't be found");
                }
                hasErrors = false;

            } catch (InvalidOrderException | PersistanceException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
        return number;
    }

    // **** HELPER METHODS END **** //
}
