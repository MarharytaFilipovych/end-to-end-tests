package com.qa.margo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qa.margo.models.Sort;

import java.util.List;

import static com.qa.margo.utils.Constants.label;

public class InventoryPage extends BasePage {

    private final Locator cartBadge;
    private final Locator cartLink;
    private final Locator sortDropdown;

    public InventoryPage(Page page) {
        super(page);
        this.cartLink = page.locator(label("shopping-cart-link"));
        this.cartBadge = page.locator(label("shopping-cart-badge"));
        this.sortDropdown = page.locator(label("product-sort-container"));
    }

    private String toSlug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }

    public void addProductToCart(String productName) {
        page.locator(label("add-to-cart-" + toSlug(productName))).click();
    }

    public void removeProductFromCart(String productName) {
        page.locator(label("remove-" + toSlug(productName))).click();
    }

    public String getCartBadgeCount() {
        return cartBadge.isVisible() ? cartBadge.textContent() : "0";
    }

    public CartPage goToCart() {
        cartLink.click();
        return new CartPage(page);
    }

    public void sortBy(Sort sort) {
        sortDropdown.selectOption(sort.getValue());
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

    public Locator sortDropdown() {
        return sortDropdown;
    }
}
