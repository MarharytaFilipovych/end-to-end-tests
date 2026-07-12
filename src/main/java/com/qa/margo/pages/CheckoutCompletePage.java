package com.qa.margo.pages;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.qa.margo.utils.Constants.label;

public class CheckoutCompletePage extends BasePage {
    private final Locator completeHeader;
    private final Locator completeText;
    private final Locator backHomeButton;
    private final Locator generatePdfButton;

    public CheckoutCompletePage(Page page) {
        super(page);
        this.completeHeader = page.locator(label("complete-header"));
        this.completeText = page.locator(label("complete-text"));
        this.backHomeButton = page.locator(label("back-to-products"));
        this.generatePdfButton = page.locator(label("generate-pdf-order"));
    }

    public String getConfirmationMessage() {
        return completeHeader.textContent();
    }

    public String getConfirmationText() {
        return completeText.textContent();
    }

    public InventoryPage backHome() {
        backHomeButton.click();
        return new InventoryPage(page);
    }

    public Download generatePdfOrder() {
        return page.waitForDownload(generatePdfButton::click);
    }
}
