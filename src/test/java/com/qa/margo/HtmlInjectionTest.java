package com.qa.margo;

import com.microsoft.playwright.Download;
import com.qa.margo.models.CheckoutData;
import com.qa.margo.pages.CheckoutCompletePage;
import com.qa.margo.pages.CheckoutStepOnePage;
import com.qa.margo.pages.CheckoutStepTwoPage;
import com.qa.margo.pages.InventoryPage;
import com.qa.margo.pages.LoginPage;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.qa.margo.TestData.BACKPACK;
import static com.qa.margo.TestData.CHECKOUT_DATA;
import static com.qa.margo.TestData.PASSWORD;
import static com.qa.margo.TestData.STANDARD_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HtmlInjectionTest extends BaseTest {
    private InventoryPage inventory;

    @BeforeEach
    void loginAndAddItemBeforeEachTest() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        inventory = loginPage.login(STANDARD_USER, PASSWORD);
        inventory.addProductToCart(BACKPACK);
    }

    @ParameterizedTest
    @SneakyThrows
    @ValueSource(strings = {
            "<script>alert('xss')</script>",
            "<img src=x onerror=alert('xss')>",
            "<svg onload=alert('xss')>",
            "\"><script>alert(1)</script>",
            "<iframe src=javascript:alert('xss')>"
    })
    void checkoutFormDoesNotExecuteInjectedMarkup(String payload) {
        AtomicBoolean dialogFired = new AtomicBoolean(false);
        page.onDialog(dialog -> {
            dialogFired.set(true);
            dialog.dismiss();
        });

        CheckoutData checkoutData = CHECKOUT_DATA.toBuilder()
                .firstName(payload)
                .build();

        CheckoutStepOnePage stepOne = inventory.goToCart().checkout();
        stepOne.fillInfo(checkoutData);

        CheckoutStepTwoPage stepTwo = stepOne.continueToOverview();
        CheckoutCompletePage complete = stepTwo.finish();

        Download download = complete.generatePdfOrder();
        String pdfText = extractText(download.path());
        String shipToSection = extractShipToSection(pdfText);

        assertEquals(payload + " " + checkoutData.lastName() + "\n" + checkoutData.zipCode(), shipToSection,
                "Expected payload to appear as literal text inside SHIP TO section: " + payload);

        assertFalse(dialogFired.get(),
                "Injected markup executed as script. This is XSS vulnerability!");
    }

    private String extractText(Path path) throws IOException {
        try (PDDocument document = Loader.loadPDF(path.toFile())){
            return new PDFTextStripper().getText(document);
        }
    }

    private String extractShipToSection(String pdfText) {
        final String shipTo = "SHIP TO\n";
        int shipToIndex = pdfText.indexOf(shipTo);
        if (shipToIndex == -1) {
            throw new RuntimeException("Could not locate 'SHIP TO' in PDF text");
        }

        int start = shipToIndex + shipTo.length();
        int end = pdfText.indexOf("ITEMS", start);
        if (end == -1) {
            throw new RuntimeException("Could not locate ITEMS section in PDF text");
        }
        return pdfText.substring(start, end).trim();
    }
}
