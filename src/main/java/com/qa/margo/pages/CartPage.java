package com.qa.margo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.qa.margo.utils.Constants.label;

public class CartPage extends BasePage {
    private final Locator checkoutButton;
    private final Locator continueShoppingButton;

    public CartPage(Page page) {
        super(page);
        this.checkoutButton = page.locator(label("checkout"));
        this.continueShoppingButton = page.locator(label("continue-shopping"));
    }

    public int getItemCount() {
        return page.locator(label("inventory-item")).count();
    }

    private String toSlug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }

    public void removeProduct(String productName) {
        page.locator(label("remove-" + toSlug(productName))).click();
    }

    public CheckoutStepOnePage checkout() {
        checkoutButton.click();
        return new CheckoutStepOnePage(page);
    }

    public InventoryPage continueShopping() {
        continueShoppingButton.click();
        return new InventoryPage(page);
    }
}
