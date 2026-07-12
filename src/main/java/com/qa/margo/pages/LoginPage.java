package com.qa.margo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.qa.margo.utils.Constants.LINK;

public class LoginPage extends BasePage {
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        super(page);
        this.usernameField = page.locator("[data-test=\"username\"]");
        this.passwordField = page.locator("[data-test=\"password\"]");
        this.loginButton = page.locator("[data-test=\"login-button\"]");
        this.errorMessage = page.locator("[data-test=\"error\"]");
    }

    public InventoryPage login(String username, String password) {
       attemptLogin(username, password);
       return new InventoryPage(page);
    }

    public void attemptLogin(String username, String password) {
        usernameField.fill(username);
        passwordField.fill(password);
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }

    public void navigate() {
        page.navigate(LINK);
    }
}
