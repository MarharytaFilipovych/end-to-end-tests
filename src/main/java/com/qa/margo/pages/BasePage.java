package com.qa.margo.pages;

import com.microsoft.playwright.Page;
import lombok.Getter;

import static com.qa.margo.utils.Constants.label;

@Getter
public class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public String getPrimaryHeader() {
        return page.locator(label("primary-header")).textContent();
    }
}
