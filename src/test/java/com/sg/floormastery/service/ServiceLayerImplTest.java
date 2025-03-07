package com.sg.floormastery.service;

import com.sg.floormastery.dao.*;
import com.sg.floormastery.dto.Order;
import com.sg.floormastery.dto.Product;
import com.sg.floormastery.dto.Tax;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class ServiceLayerImplTest {
    private ServiceLayer service;
    Order order;

    public ServiceLayerImplTest(){
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
         service = ctx.getBean("serviceLayer", ServiceLayerImpl.class);

        order = new Order(1,
                "Diego", "TX",  new BigDecimal("4.45"), "Tiles", new BigDecimal("127.02"),
                new BigDecimal("2.25"), new BigDecimal("2.10"),
                new BigDecimal("120"), new BigDecimal("83"),
                new BigDecimal("77"),new BigDecimal("3900.12"));
    }
    @Test
    void addOrder() {

        Order orderAdded = service.addOrder(order, "10-30-2025");
        assertNotNull(orderAdded, "Order should not be null after adding");
        assertEquals(order.getOrderNumber(), orderAdded.getOrderNumber(),
                "It should return the same order number");
        assertEquals(order.getCustomerName(), orderAdded.getCustomerName(),
                "It should return the same order name");
        assertEquals(order.getState(), orderAdded.getState(),
                "It should return the same order state");
        assertEquals(order.getProductType(), orderAdded.getProductType(),
                "It should return the same order product type");
        assertEquals(0, order.getArea().compareTo(orderAdded.getArea()),
                "It should return the same order area");
        assertEquals(0, order.getTaxRate().compareTo(orderAdded.getTaxRate()),
                "It should return the same order tax rate");
        assertEquals(0, order.getCostPerSquareFoot().compareTo(orderAdded.getCostPerSquareFoot()),
                "It should return the same order cost per square foot");
        assertEquals(0, order.getLaborCostPerSquareFoot().compareTo(orderAdded.getLaborCostPerSquareFoot()),
                "It should return the same order labor cost per square foot");
        assertEquals(0, order.getMaterialCost().compareTo(orderAdded.getMaterialCost()),
                "It should return the same order material cost");
        assertEquals(0, order.getLaborCost().compareTo(orderAdded.getLaborCost()),
                "It should return the same order labor cost");
        assertEquals(0, order.getTax().compareTo(orderAdded.getTax()),
                "It should return the same order material tax cost");
        assertEquals(0, order.getTotal().compareTo(orderAdded.getTotal()),
                "It should return the same order total cost");

    }

    @Test
    void editOrder() {

        Order orderEdited = service.editOrder(order, "10-30-2025");
        assertNotNull(orderEdited, "Order should not be null after editing");
        assertEquals(order.getOrderNumber(), orderEdited.getOrderNumber(),
                "It should return the same order number");
        assertEquals(order.getCustomerName(), orderEdited.getCustomerName(),
                "It should return the same order name");
        assertEquals(order.getState(), orderEdited.getState(),
                "It should return the same order state");
        assertEquals(order.getProductType(), orderEdited.getProductType(),
                "It should return the same order product type");
        assertEquals(0, order.getArea().compareTo(orderEdited.getArea()),
                "It should return the same order area");
        assertEquals(0, order.getTaxRate().compareTo(orderEdited.getTaxRate()),
                "It should return the same order tax rate");
        assertEquals(0, order.getCostPerSquareFoot().compareTo(orderEdited.getCostPerSquareFoot()),
                "It should return the same order cost per square foot");
        assertEquals(0, order.getLaborCostPerSquareFoot().compareTo(orderEdited.getLaborCostPerSquareFoot()),
                "It should return the same order labor cost per square foot");
        assertEquals(0, order.getMaterialCost().compareTo(orderEdited.getMaterialCost()),
                "It should return the same order material cost");
        assertEquals(0, order.getLaborCost().compareTo(orderEdited.getLaborCost()),
                "It should return the same order labor cost");
        assertEquals(0, order.getTax().compareTo(orderEdited.getTax()),
                "It should return the same order material tax cost");
        assertEquals(0, order.getTotal().compareTo(orderEdited.getTotal()),
                "It should return the same order total cost");
    }

    @Test
    void removeOrder() {
        Order orderRemoved = service.removeOrder(order, "10-30-2025");
        assertNotNull(orderRemoved, "Order should not be null after removing");
        assertEquals(order.getOrderNumber(), orderRemoved.getOrderNumber(),
                "It should return the same order number");
        assertEquals(order.getCustomerName(), orderRemoved.getCustomerName(),
                "It should return the same order name");
        assertEquals(order.getState(), orderRemoved.getState(),
                "It should return the same order state");
        assertEquals(order.getProductType(), orderRemoved.getProductType(),
                "It should return the same order product type");
        assertEquals(0, order.getArea().compareTo(orderRemoved.getArea()),
                "It should return the same order area");
        assertEquals(0, order.getTaxRate().compareTo(orderRemoved.getTaxRate()),
                "It should return the same order tax rate");
        assertEquals(0, order.getCostPerSquareFoot().compareTo(orderRemoved.getCostPerSquareFoot()),
                "It should return the same order cost per square foot");
        assertEquals(0, order.getLaborCostPerSquareFoot().compareTo(orderRemoved.getLaborCostPerSquareFoot()),
                "It should return the same order labor cost per square foot");
        assertEquals(0, order.getMaterialCost().compareTo(orderRemoved.getMaterialCost()),
                "It should return the same order material cost");
        assertEquals(0, order.getLaborCost().compareTo(orderRemoved.getLaborCost()),
                "It should return the same order labor cost");
        assertEquals(0, order.getTax().compareTo(orderRemoved.getTax()),
                "It should return the same order material tax cost");
        assertEquals(0, order.getTotal().compareTo(orderRemoved.getTotal()),
                "It should return the same order total cost");
    }

    @Test
    void exportAllData() {
        assertDoesNotThrow(() -> service.exportAllData(),
                "Exporting all data should not throw an exception.");
    }

    @Test
    void testIsFutureDateValid(){
        String validDate = "10-30-2025";
        try{
            assertTrue(service.isFutureDateValid(validDate), "Date meets the conditions");
        }
        catch (InvalidOrderException e){
            fail("The valid date wasn't accepted");
        }
    }

    @Test
    void testIsFutureDateNotValid(){
        String date = "01/01/2003"; // Date already passed
        try{
            service.isFutureDateValid(date);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid date was accepted");
    }

    @Test
    void testIsDateValid(){
        String validDate = "06-02-2013";
        try{
            assertTrue(service.isDateValid(validDate), "Date meets the conditions");
        }
        catch (InvalidOrderException e){
            fail("The valid date wasn't accepted");
        }
    }

    @Test
    void testIsDateNotValid(){
        String date = "10/30/2025"; // Wrong format
        try{
            service.isDateValid(date);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid date was accepted");
    }


    @Test
    void testIsNameValid(){
        String name = "Diego La Rosa Giraud";
        try{
            assertTrue(service.isNameValid(name), "Name meets the conditions");
        }
        catch (InvalidOrderException e){
            fail("The valid name wasn't accepted");
        }
    }

    @Test
    void testIsNameNotValid(){
        String name = "D!ego La Rosa Giraud";
        try{
            service.isNameValid(name);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid name was accepted");
    }

    @Test
    void testIsStateValid(){
        String state = "FL";
        try{
            assertTrue(service.isStateValid(state), "The state was correctly found.");
        }
        catch (InvalidOrderException e){
            fail("The valid state wasn't accepted");
        }
    }

    @Test
    void testIsStateNotValid(){
        String state = "TK";
        try{
            service.isStateValid(state);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid state was accepted");
    }

    @Test
    void testIsProductValid(){
        String product = "Cardboard";
        try{
            assertTrue(service.isProductValid(product), "The product was correctly found.");
        }
        catch (InvalidOrderException e){
            fail("The valid product wasn't accepted");
        }
    }

    @Test
    void testIsProductNotValid(){
        String product = "NOT VALID";
        try{
            service.isProductValid(product);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid product was accepted");
    }

    @Test
    void testIsAreaValid(){
        String validArea = "120.5";
        try{
            assertTrue(service.isAreaValid(validArea), "The area should be valid.");
        }
        catch (InvalidOrderException e){
            fail("The valid area wasn't accepted.");
        }
    }

    @Test
    void testIsAreaNotValid(){
        String invalidArea = "0";
        try{
            service.isAreaValid(invalidArea);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid area was accepted.");
    }

    @Test
    void testIsOrderNumberValid(){
        String validOrderNumber = "1";
        try{
            assertTrue(service.isOrderNumberValid(validOrderNumber), "The order number should be valid.");
        }
        catch (InvalidOrderException e){
            fail("The valid order number wasn't accepted.");
        }
    }

    @Test
    void testIsOrderNumberNotValid(){
        String invalidOrderNumber = "NOT A NUMBER";
        try{
            service.isOrderNumberValid(invalidOrderNumber);
        }
        catch (InvalidOrderException e){
            return;
        }
        fail("The invalid order number was accepted.");
    }

    @Test
    void testCalMaterialCost() {
        BigDecimal area = new BigDecimal("100");
        BigDecimal costPerSquareFoot = new BigDecimal("2.50");

        BigDecimal expected = new BigDecimal("250.00");
        assertEquals(expected, service.calMaterialCost(area, costPerSquareFoot),
                "Material cost should be area * cost per square foot.");
    }

    @Test
    void testCalLaborCost() {
        BigDecimal area = new BigDecimal("150");
        BigDecimal laborPerSquareFoot = new BigDecimal("3.75");

        BigDecimal expected = new BigDecimal("562.50");
        assertEquals(expected, service.calLaborCost(area, laborPerSquareFoot),
                "Labor cost should be area * labor per square foot.");
    }

    @Test
    void testCalTax() {
        BigDecimal materialCost = new BigDecimal("200.00");
        BigDecimal laborCost = new BigDecimal("300.00");
        BigDecimal taxRate = new BigDecimal("7.5");

        BigDecimal expected = new BigDecimal("37.50"); // (200 + 300) * 0.075 = 37.50


        assertEquals(expected, service.calTax(materialCost, laborCost, taxRate),
                "Tax should be (material + labor cost) * tax rate.");
    }
    @Test
    void testCalTotal() {
        BigDecimal materialCost = new BigDecimal("500.00");
        BigDecimal laborCost = new BigDecimal("400.00");
        BigDecimal tax = new BigDecimal("67.50");

        BigDecimal expected = new BigDecimal("967.50");
        assertEquals(expected, service.calTotal(materialCost, laborCost, tax),
                "Total should be material + labor + tax.");
    }

    @Test
    void testDoAllOrderCalculations() {
        Tax tax = new Tax("TX", "Texas",new BigDecimal("6.25"));
        Product product = new Product("Wood", new BigDecimal("5.00"), new BigDecimal("4.50"));
        BigDecimal area = new BigDecimal("200");

        Map<String, BigDecimal> calculations = service.doAllOrderCalculations(tax, product, area);

        assertEquals(new BigDecimal("1000.00"), calculations.get("materialCost"),
                "Material cost should be area * cost per square foot.");
        assertEquals(new BigDecimal("900.00"), calculations.get("laborCost"),
                "Labor cost should be area * labor per square foot.");
        assertEquals(new BigDecimal("118.75"), calculations.get("taxCost"),
                "Tax should be correct.");
        assertEquals(new BigDecimal("2018.75"), calculations.get("total"),
                "Total cost should be correct.");
    }


    @Test
    void testGetOrders() {
        service.addOrder(order, "10-30-2025"); // Ensure there's an order

        List<Order> orders = service.getOrders("10-30-2025");
        assertFalse(orders.isEmpty(), "Orders list should not be empty.");
        assertEquals(1, orders.size(), "There should be exactly one order.");
        assertEquals(order.getOrderNumber(), orders.get(0).getOrderNumber(),
                "The correct order should be returned.");
    }

    @Test
    void testGetProducts() {
        List<Product> products = service.getProducts();
        assertFalse(products.isEmpty(), "Product list should not be empty.");
    }

    @Test
    void testGetTax() {
        Tax tax = service.getTax("FL");
        assertNotNull(tax, "Tax should not be null for a valid state.");
        assertEquals("FL", tax.getStateAbbreviation(), "State code should match.");
    }

    @Test
    void testGetProduct() {
        Product product = service.getProduct("Cardboard");
        assertNotNull(product, "Product should not be null for a valid type.");
        assertEquals("Cardboard", product.getProductType(), "Product type should match.");
    }

    @Test
    void testGetOrder() {
        service.addOrder(order, "10-30-2025");

        Order retrievedOrder = service.getOrder(order.getOrderNumber());
        assertNotNull(retrievedOrder, "Order should not be null.");
        assertEquals(order.getOrderNumber(), retrievedOrder.getOrderNumber(),
                "Order number should match.");
    }

    @Test
    void testGetNextOrderNumber() {
        int orderNumber = service.getNextOrderNumber();
        assertTrue(orderNumber > 0, "Order number should be greater than 0.");
    }

    @Test
    void testExportAllData(){
        service.exportAllData();
    }

    }