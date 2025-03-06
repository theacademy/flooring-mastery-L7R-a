package com.sg.floormastery.ui;

import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import com.sg.floormastery.service.InvalidOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class FloorView {
    private UserIOConsoleImpl io;

    @Autowired
    public FloorView(UserIOConsoleImpl io){
        this.io = io;
    }

    public int displayMenu() throws InvalidOrderException{

        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("  * <<Flooring Program>>");
        io.print("  * 1. Display Orders");
        io.print("  * 2. Add an Order");
        io.print("  * 3. Edit an Order");
        io.print("  * 4. Remove an Order");
        io.print("  * 5. Export All Data");
        io.print("  * 6. Quit");
        io.print("  *");
        io.print("  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");

        try{
            return io.readInt("Please select from the above choices.", 1, 6);
        }
        catch (NumberFormatException e){
            throw new InvalidOrderException("ERROR: Input is not an integer.");
        }
    }

    // **** ACTION BANNERS START **** //

    public void displayOrdersBanner(){
        io.print("Starting the displaying order command...");
    }

    public void displayAddOrderBanner(){
        io.print("In order to add an order we need to get the following information");
    }

    public void displayEditOrderBanner(){
        io.print("Please fill up the following information to update the order. Leave field in blank to keep it as it is");
    }

    public void displayRemoveOrderBanner(){
        io.print("In order to delete an order we need to get the following information");
    }

    public void displayExitBanner(){
        io.print("Thank you for using our services!");
    }

    // **** ACTION BANNERS END **** ///




    // **** GET USER INPUT START **** //

    public String getUserOrderNumber() {
        return io.readString("Please enter the order number");
    }

    public String getUserOrderDate() {
        return io.readString("Please enter the date of the order. (MM-DD-YYYY format)");
    }

    public String getUserOrderName(){
        return io.readString("Please enter the customer name");
    }

    public String getUserOrderState(){
        return io.readString("Please enter the state. Type only the abbreviation of the state");
    }

    public String getUserProductType(){
        return io.readString("Please enter the product type you would like to order");
    }

    public String getUserOrderArea(){
        return io.readString("Please enter the area.");
    }

    // **** GET USER INPUT END **** //


    // **** SHOW OLD ORDER INFORMATION (EDITING) START **** //

    public void displayCurrentName(Order order){
        io.print("The current name is:" + order.getCustomerName());
    }

    public void displayCurrentState(Order order){
        io.print("The current state is: " + order.getState());
    }

    public void displayCurrentProductType(Order order){
        io.print("The current product type is:" + order.getProductType());
    }

    public void displayCurrentArea(Order order){
        io.print("The current area is: " + order.getArea());
    }

    // **** SHOW OLD ORDER INFORMATION (EDITING) END **** //


    // **** SHOW FULL PRODUCT OR ORDER INFORMATION START **** //

    public void displayProducts(List<Product> products){
        io.print("The following is the list of products available organized by type," +
                " cost per square foot, and labor cost per square foot");

        products.stream().
                forEach((product)->io.print(
                        product.getProductType() + " " + product.getCostPerSquareFoot()+ "$ " +
                                product.getLaborCostPerSquareFoot() + "$"));
    }

    // Displays a single order information
    public void displayOrderInformation(Order order) {
        io.print("Here is the information for this order:");
        io.print("Customer name: "+order.getCustomerName());
        io.print("State: "+order.getState());
        io.print("Tax rate: "+order.getTaxRate());
        io.print("Product type: "+order.getProductType());
        io.print("Area: "+order.getArea());
        io.print("Cost per square foot: "+order.getCostPerSquareFoot());
        io.print("Labor cost per square foot: "+order.getLaborCostPerSquareFoot());
        io.print("Material cost: "+order.getMaterialCost());
        io.print("Labor cost: "+order.getLaborCost());
        io.print("Tax: "+order.getTax());
        io.print("Total: "+order.getTotal());
    }

    // Displays many orders information
    public void displayOrders(List<Order> orders){
        io.print("Here are your orders. The details of each order are separated with ',' and they represent the following in order:");
        io.print("ID, customer name, state, tax rate, product type, area, cost per square foot, labor cost per square foot,"+
                "material cost, labor cost, tax, and total cost respectively\n");
        orders.stream().
                forEach((order)->io.print(order.getOrderNumber() + ", " + order.getCustomerName() + ", "+
                        order.getState() + ", " + order.getTaxRate() + "%, " + order.getProductType() + ", " +
                        order.getArea() + " sq ft, " + order.getCostPerSquareFoot() + "$, " + order.getLaborCostPerSquareFoot()
                        + "$, "+  order.getMaterialCost() + "$, " + order.getLaborCost() + "$, " + order.getTax() + "$, " +
                        order.getTotal()+"$\n"));
        displayPressEnterToContinue();
    }

    // **** SHOW FULL PRODUCT OR ORDER INFORMATION END **** //


    // **** SHOW RESULT OR CONFIRM ACTIONS START **** //

    public void displayActionResult(Order result, String action){
        if(result == null){
            io.readString("The order couldn't be" + action);
        }
        else{
            io.print("The order was " + action + " successfully!");
        }
    }

    public void displayErrorMessage(String message){
        io.print(message+"\n");
    }

    public void displaySuccessExportation() {
        io.print("Data exported successfully! Check a file called ExportedData inside the Backup folder.");
    }

    public String displayPressEnterToContinue(){
        return io.readString("Press enter to continue");
    }

    public String confirmAction(String action){
        return io.readString("Do you want to " +action + " the order? (Y/N): ");
    }
    // **** SHOW RESULT OR CONFIRM ACTIONS END **** //





}
