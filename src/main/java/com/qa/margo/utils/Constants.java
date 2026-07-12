package com.qa.margo.utils;

public class Constants {
    private Constants () { }

    private static final String DATA_TEST_LABEL = "[data-test=\"%s\"]";
    public static final String LINK = "https://www.saucedemo.com/";

    public static String label(String value) {
        return DATA_TEST_LABEL.formatted(value);
    }
}
