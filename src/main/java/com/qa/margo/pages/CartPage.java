package com.qa.margo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

import static com.qa.margo.utils.Constants.ZERO;
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

    public List<String> getProductNames() {
        return page.locator(label("inventory-item-name")).allTextContents();
    }

    public List<Double> getProductPrices() {
        return page.locator(label("inventory-item-price")).allTextContents()
                .stream()
                .map(price -> price.replace("$", "").trim())
                .map(Double::parseDouble)
                .toList();
    }

    public String getCartBadgeCount() {
        Locator badge = page.locator(label("shopping-cart-badge"));
        return badge.isVisible() ? badge.textContent() : ZERO;
    }
}
