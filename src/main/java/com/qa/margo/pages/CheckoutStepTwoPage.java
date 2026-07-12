package com.qa.margo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.qa.margo.utils.Constants.label;

public class CheckoutStepTwoPage extends BasePage {
    private final Locator finishButton;
    private final Locator cancelButton;
    private final Locator subtotalLabel;
    private final Locator taxLabel;
    private final Locator totalLabel;
    private final Locator paymentInfoValue;
    private final Locator shippingInfoValue;
    private final Locator itemCount;

    public CheckoutStepTwoPage(Page page) {
        super(page);
        this.finishButton = page.locator(label("finish"));
        this.cancelButton = page.locator(label("cancel"));
        this.subtotalLabel = page.locator(label("subtotal-label"));
        this.taxLabel = page.locator(label("tax-label"));
        this.totalLabel = page.locator(label("total-label"));
        this.paymentInfoValue = page.locator(label("payment-info-value"));
        this.shippingInfoValue = page.locator(label("shipping-info-value"));
        this.itemCount = page.locator(label("inventory-item"));
    }

    public CheckoutCompletePage finish() {
        finishButton.click();
        return new CheckoutCompletePage(page);
    }

    public InventoryPage cancel() {
        cancelButton.click();
        return new InventoryPage(page);
    }

    public String getSubtotal() {
        return subtotalLabel.textContent();
    }

    public String getTax() {
        return taxLabel.textContent();
    }

    public String getTotal() {
        return totalLabel.textContent();
    }

    public String getPaymentInfo() {
        return paymentInfoValue.textContent();
    }

    public String getShippingInfo() {
        return shippingInfoValue.textContent();
    }

    public int getItemCount() {
        return itemCount.count();
    }
}
