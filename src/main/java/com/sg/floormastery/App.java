package com.sg.floormastery;

import com.sg.floormastery.controller.FloorController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String [] args){
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sg.floormastery");
        appContext.refresh();

        FloorController controller = appContext.getBean("floorController", FloorController.class);
        controller.run();
    }
}
