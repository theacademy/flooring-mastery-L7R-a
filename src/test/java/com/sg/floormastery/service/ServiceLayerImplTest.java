package com.sg.floormastery.service;

import com.sg.floormastery.controller.FloorController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServiceLayerImplTest {
    public ServiceLayerImplTest() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.sg.floormastery");
        appContext.refresh();

        FloorController controller = appContext.getBean("floorController", FloorController.class);
        controller.run();
    }
}
