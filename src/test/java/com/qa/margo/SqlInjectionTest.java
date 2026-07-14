package com.qa.margo;

import com.qa.margo.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.qa.margo.TestData.PASSWORD;
import static com.qa.margo.TestData.STANDARD_USER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SqlInjectionTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    void navigateBeforeEachTest() {
        loginPage = new LoginPage(page);
        loginPage.navigate();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "' OR '1'='1",
            "' OR '1'='1' --",
            "admin'--",
            "' OR 1=1#",
            "'; DROP TABLE users; --",
            "' UNION SELECT NULL, NULL --"
    })
    void loginRejectsSqlInjectionInUsername(String payload) {
        loginPage.attemptLogin(payload, PASSWORD);

        String error = loginPage.getErrorMessage();
        assertTrue(error.startsWith("Epic sadface:"),
                "Expected an auth error, got: " + error);
        assertFalse(page.url().contains("inventory.html"),
                "SQL injection payload must not grant access to inventory page");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "' OR '1'='1",
            "' OR '1'='1' --",
            "'; DROP TABLE users; --"
    })
    void loginRejectsSqlInjectionInPassword(String payload) {
        loginPage.attemptLogin(STANDARD_USER, payload);

        String error = loginPage.getErrorMessage();
        assertTrue(error.startsWith("Epic sadface:"),
                "Expected an auth error, got: " + error);
        assertFalse(page.url().contains("inventory.html"));
    }
}
