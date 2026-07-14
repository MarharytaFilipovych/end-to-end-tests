package com.qa.margo;

import com.microsoft.playwright.Download;
import com.qa.margo.models.Sort;
import com.qa.margo.pages.CartPage;
import com.qa.margo.pages.CheckoutCompletePage;
import com.qa.margo.pages.CheckoutStepOnePage;
import com.qa.margo.pages.CheckoutStepTwoPage;
import com.qa.margo.pages.InventoryPage;
import com.qa.margo.pages.LoginPage;
import com.qa.margo.models.CheckoutData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.qa.margo.TestData.BACKPACK;
import static com.qa.margo.TestData.BIKE_LIGHT;
import static com.qa.margo.TestData.CHECKOUT_DATA;
import static com.qa.margo.TestData.PASSWORD;
import static com.qa.margo.utils.Constants.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.qa.margo.TestData.STANDARD_USER;

public class CheckoutTest extends BaseTest {
    private InventoryPage inventory;

    @BeforeEach
    void loginAndAddItemBeforeEachTest() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        inventory = loginPage.login(STANDARD_USER, PASSWORD);
        inventory.addProductToCart(BACKPACK);
        inventory.addProductToCart(BIKE_LIGHT);
    }

    @Test
    void completeCheckoutFlow() {
        CartPage cart = inventory.goToCart();
        assertEquals(2, cart.getItemCount());

        CheckoutStepOnePage stepOne = cart.checkout();
        stepOne.fillInfo(CHECKOUT_DATA);

        CheckoutStepTwoPage stepTwo = stepOne.continueToOverview();

        assertEquals(2, stepTwo.getItemCount());
        assertTrue(stepTwo.getPaymentInfo().contains("SauceCard"));
        assertTrue(stepTwo.getShippingInfo().contains("Free Pony Express"));
        assertTrue(cart.getProductNames().containsAll(List.of(BACKPACK, BIKE_LIGHT)));
        assertFalse(cart.getProductPrices().isEmpty());

        CheckoutCompletePage complete = stepTwo.finish();

        assertTrue(complete.getConfirmationMessage().contains("Thank you for your order"));

        assertTrue(complete.getConfirmationText().contains("Your order has been dispatched"));

        InventoryPage backToProducts = complete.backHome();
        assertEquals(ZERO, backToProducts.getCartBadgeCount());
    }

    @Test
    void checkoutBlocksWithMissingFirstName() {
        CheckoutStepOnePage stepOne = checkout(CHECKOUT_DATA.toBuilder()
                .firstName("").build());
        stepOne.continueToOverview();

        assertEquals("Error: First Name is required", stepOne.getErrorMessage());
    }

    @Test
    void checkoutBlocksWithMissingLastName() {
        CheckoutStepOnePage stepOne = checkout(CHECKOUT_DATA.toBuilder()
                .lastName("").build());
        stepOne.continueToOverview();

        assertEquals("Error: Last Name is required", stepOne.getErrorMessage());
    }

    @Test
    void checkoutBlocksWithMissingPostalCode() {
        CheckoutStepOnePage stepOne = checkout(CHECKOUT_DATA.toBuilder()
                .zipCode("").build());
        stepOne.continueToOverview();

        assertEquals("Error: Postal Code is required", stepOne.getErrorMessage());
    }

    @Test
    void cancelOnCheckoutStepOneReturnsToCart() {
        CheckoutStepOnePage stepOne = inventory.goToCart().checkout();

        CartPage cart = stepOne.cancel();

        assertEquals(2, cart.getItemCount());
    }

    @Test
    void cancelOnCheckoutOverviewReturnsToInventory() {
        CheckoutStepTwoPage stepTwo = checkout().continueToOverview();

        InventoryPage backToInventory = stepTwo.cancel();

        assertEquals("2", backToInventory.getCartBadgeCount());
    }

    @Test
    void canGeneratePdfOrderConfirmation() {
        CheckoutCompletePage complete = checkout().continueToOverview().finish();

        Download download = complete.generatePdfOrder();

        assertTrue(download.suggestedFilename().endsWith(".pdf"));
    }

    @Test
    void sortProductsByNameAToZ() {
        inventory.sortBy(Sort.NAME_ASC);

        assertThat(inventory.sortDropdown()).containsText("Name (A to Z)");

        List<String> names = inventory.getProductNames();
        List<String> expected = names.stream().sorted().toList();
        assertEquals(expected, names);
    }

    @Test
    void sortProductsByNameZToA() {
        inventory.sortBy(Sort.NAME_DESC);

        assertThat(inventory.sortDropdown()).containsText("Name (Z to A)");

        List<String> names = inventory.getProductNames();
        List<String> expected = names.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        assertEquals(expected, names);
    }

    @Test
    void sortProductsByPriceHighToLow() {
        inventory.sortBy(Sort.PRICE_HIGH);

        assertThat(inventory.sortDropdown()).containsText("Price (high to low)");

        List<Double> prices = inventory.getProductPrices();
        List<Double> expected = prices.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        assertEquals(expected, prices);
    }

    @Test
    void sortProductsByPriceLowToHigh() {
        inventory.sortBy(Sort.PRICE_LOW);

        assertThat(inventory.sortDropdown()).containsText("Price (low to high)");

        List<Double> prices = inventory.getProductPrices();
        List<Double> expected = prices.stream().sorted().toList();
        assertEquals(expected, prices);
    }

    @Test
    void checkoutOverviewShowsCorrectPriceTotals() {
        CheckoutStepTwoPage stepTwo = checkout().continueToOverview();

        double itemTotal = parseMoney(stepTwo.getSubtotal());
        double tax = parseMoney(stepTwo.getTax());
        double total = parseMoney(stepTwo.getTotal());

        assertEquals(itemTotal + tax, total, 0.01);
    }

    private double parseMoney(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    private CheckoutStepOnePage checkout(CheckoutData checkoutData) {
        CheckoutStepOnePage stepOne = inventory.goToCart().checkout();
        stepOne.fillInfo(checkoutData);
        return stepOne;
    }

    private CheckoutStepOnePage checkout() {
        CheckoutStepOnePage stepOne = inventory.goToCart().checkout();
        stepOne.fillInfo(CHECKOUT_DATA);
        return stepOne;
    }
}
