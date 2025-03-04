package com.sg.floormastery.ui;

import com.sg.floormastery.dto.Order;
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
        orders.stream().forEach((order)->io.print(order.getCustomerName()));
    }

    public void displayAddOrderBanner(){

    }

    public void displayEditOrderBanner(){

    }

    public void displayRemoveOrderBanner(){

    }

    public void displayExportAllDataBanner(){

    }

    public void displayExitBanner(){

    }

    public void displaySuccess(Order result, String action){

    }


    public void displayError(String message){

    }

}
