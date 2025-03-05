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

    public int displayMenu(){

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

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public String displayOrdersBanner(){
        return io.readString("Enter the date you want to see orders (Date must be of format MM-DD-YYYY): ");
    }

    public void displayOrders(List<Order> orders){
        io.print("Here are your orders");
        orders.stream().
                forEach((order)->io.print("Order ID:" + order.getOrderNumber() + "Customer Name:" + order.getCustomerName()));
    }



    public String getUserOrderNumber() {
        return io.readString("Please enter the order number");
    }

    public String getUserOrderDate() {
        return io.readString("Please enter the date of the order. (MM-DD-YYYY format)");
    }

    public String getUserOrderName(){
        return io.readString("Great, now we need your name");
    }

    public String getUserOrderState(){
        return io.readString("Awesome, in which state is the order. Type only the abbreviation of the state?");
    }

    public String getUserOrderProduct(List<Product> products){
        io.print("The following is the list of products available organized by type," +
                " cost per square foot, and labor cost per square foot");

        products.stream().
                forEach((product)->io.print(
                        product.getProductType() + " " + product.getCostPerSquareFoot()+ "$ " +
                                product.getLaborCostPerSquareFoot() + "$"));
        return io.readString("Please enter the product type you would like to order");
    }

    public String getUserOrderArea() throws InvalidOrderException {
        return io.readString("Finally, type the area you want to get");
    }


    public void displayAddOrderBanner(){
        io.print("In order to add an order we need to get the following information");
    }

    public void displayEditOrderBanner(){

    }

    public void displayRemoveOrderBanner(){
        io.print("In order to delete an order we need to get the following information");
    }

    public void displayExportAllDataBanner(){

    }

    public void displayExitBanner(){

    }

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

    public String confirmAction(String action){
        return io.readString("Do you want to " +action + " the order? (Y/N): ");
    }
}
