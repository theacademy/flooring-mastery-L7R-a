package com.sg.floormastery.controller;

import com.sg.floormastery.dto.Order;
import com.sg.floormastery.service.ServiceLayer;
import com.sg.floormastery.ui.FloorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private void addOrder(){

    }

    private void editOrder(){

    }

    private void removeOrder(){

    }

    private void exportAllData(){

    }

    private void quit(){
        return;
    }
}
