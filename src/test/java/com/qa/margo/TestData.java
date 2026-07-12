package com.qa.margo;

import com.qa.margo.models.CheckoutData;

public class TestData {

    private TestData() { }

    public static final String PASSWORD = "secret_sauce";
    public static final String STANDARD_USER = "standard_user";
    public static final String BOLT_T_SHIRT = "Sauce Labs Bolt T-Shirt";
    public static final String BACKPACK = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    public static final String ONESIE = "Sauce Labs Onesie";
    public static final String RED_T_SHIRT = "Test.allTheThings() T-Shirt (Red)";

    public static final CheckoutData CHECKOUT_DATA = CheckoutData.builder()
            .firstName("Margosha")
            .lastName("Filipovych")
            .zipCode("678678")
            .build();
}
