package com.qa.margo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qa.margo.models.CheckoutData;

import static com.qa.margo.utils.Constants.label;

public class CheckoutStepOnePage extends BasePage {
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator postalCodeInput;
    private final Locator continueButton;
    private final Locator cancelButton;
    private final Locator errorMessage;

    public CheckoutStepOnePage(Page page) {
        super(page);
        this.firstNameInput = page.locator(label("firstName"));
        this.lastNameInput = page.locator(label("lastName"));
        this.postalCodeInput = page.locator(label("postalCode"));
        this.continueButton = page.locator(label("continue"));
        this.cancelButton = page.locator(label("cancel"));
        this.errorMessage = page.locator(label("error"));
    }

    public void fillInfo(CheckoutData checkoutData) {
        firstNameInput.fill(checkoutData.firstName());
        lastNameInput.fill(checkoutData.lastName());
        postalCodeInput.fill(checkoutData.zipCode());
    }

    public CheckoutStepTwoPage continueToOverview() {
        continueButton.click();
        return new CheckoutStepTwoPage(page);
    }

    public CartPage cancel() {
        cancelButton.click();
        return new CartPage(page);
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }
}
