package com.qa.margo;

import com.qa.margo.pages.InventoryPage;
import com.qa.margo.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.qa.margo.TestData.PASSWORD;
import static com.qa.margo.TestData.STANDARD_USER;
import static com.qa.margo.utils.Constants.LINK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @ParameterizedTest
    @ValueSource(strings = {STANDARD_USER, "problem_user", "performance_glitch_user", "error_user", "visual_user"})
    void successfulLoginWithStandardUser(String user) {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();

        InventoryPage inventory = loginPage.login(user, PASSWORD);

        assertTrue(inventory.getPrimaryHeader().contains("Swag Labs"));
    }

    @Test
    void loginFailsWithLockedOutUser() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();

        loginPage.attemptLogin("locked_out_user", PASSWORD);

        assertEquals("Epic sadface: Sorry, this user has been locked out.", loginPage.getErrorMessage());
    }

    @Test
    void loginFailsWithEmptyUsername() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();

        loginPage.attemptLogin("", PASSWORD);

        assertEquals("Epic sadface: Username is required", loginPage.getErrorMessage());
    }

    @Test
    void loginFailsWithEmptyPassword() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();

        loginPage.attemptLogin(STANDARD_USER, "");

        assertEquals("Epic sadface: Password is required", loginPage.getErrorMessage());
    }

    @Test
    void loginFailsWithWrongCredentials() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();

        loginPage.attemptLogin(STANDARD_USER, "wrong_password");

        assertEquals("Epic sadface: Username and password do not match any user in this service",
                loginPage.getErrorMessage());
    }

    @Test
    void directUrlAccessWithoutLoginRedirectsToLoginPage() {
        page.navigate(LINK + "/inventory.html");

        LoginPage loginPage = new LoginPage(page);

        assertEquals("Epic sadface: You can only access '/inventory.html' when you are logged in.",
                loginPage.getErrorMessage());
    }
}
