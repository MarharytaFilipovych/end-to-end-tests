package com.qa.margo;

import com.qa.margo.pages.CartPage;
import com.qa.margo.pages.InventoryPage;
import com.qa.margo.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.qa.margo.TestData.BACKPACK;
import static com.qa.margo.TestData.BIKE_LIGHT;
import static com.qa.margo.TestData.BOLT_T_SHIRT;
import static com.qa.margo.TestData.FLEECE_JACKET;
import static com.qa.margo.TestData.ONESIE;
import static com.qa.margo.TestData.PASSWORD;
import static com.qa.margo.TestData.RED_T_SHIRT;
import static com.qa.margo.TestData.STANDARD_USER;
import static com.qa.margo.utils.Constants.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void loginBeforeEachTest() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.navigate();
        inventory = loginPage.login(STANDARD_USER, PASSWORD);
    }

    @ParameterizedTest
    @ValueSource(strings = {BACKPACK, BIKE_LIGHT, BOLT_T_SHIRT, RED_T_SHIRT, ONESIE, FLEECE_JACKET})
    void addSingleProductToCart(String product) {
        inventory.addProductToCart(product);

        checkItemCountOnCartIcon(1);
        checkItemCountOnCartPage(1);
    }

    @Test
    void addMultipleProductsToCart() {
        List<String> products = List.of(BACKPACK, BIKE_LIGHT, BOLT_T_SHIRT);
        products.forEach(p -> inventory.addProductToCart(p));

        checkItemCountOnCartIcon(products.size());
        checkItemCountOnCartPage(products.size());
    }

    @Test
    void addAllSixProductsToCart() {
        List<String> products = List.of(BACKPACK, BIKE_LIGHT, BOLT_T_SHIRT,
                FLEECE_JACKET, ONESIE, RED_T_SHIRT);

        products.forEach(p -> inventory.addProductToCart(p));

        checkItemCountOnCartIcon(products.size());
        checkItemCountOnCartPage(products.size());
    }

    @Test
    void toggleAddAndRemoveDoesNotDuplicateItem() {
        inventory.addProductToCart(BACKPACK);
        checkItemCountOnCartIcon(1);
        checkItemCountOnCartPage(1);

        inventory.removeProductFromCart(BACKPACK);
        checkItemCountOnCartIcon(0);
        checkItemCountOnCartPage(0);

        inventory.addProductToCart(BACKPACK);
        checkItemCountOnCartIcon(1);
        checkItemCountOnCartPage(1);
    }

    @Test
    void removeProductFromCartPage() {
        inventory.addProductToCart(BACKPACK);
        inventory.addProductToCart(BIKE_LIGHT);

        CartPage cart = inventory.goToCart();
        assertEquals(2, cart.getItemCount());

        cart.removeProduct(BACKPACK);
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void continueShoppingReturnsToInventoryPageWithCartIntact() {
        inventory.addProductToCart(BACKPACK);
        CartPage cart = inventory.goToCart();

        InventoryPage backToInventory = cart.continueShopping();

        assertEquals("1", backToInventory.getCartBadgeCount());
    }

    @Test
    void removeProductFromInventoryPage() {
        inventory.addProductToCart(BACKPACK);
        checkItemCountOnCartIcon(1);

        inventory.removeProductFromCart(BACKPACK);
        checkItemCountOnCartIcon(0);
    }

    @Test
    void removeProductFromCartPageResultsInEmptyCart() {
        inventory.addProductToCart(BIKE_LIGHT);
        checkItemCountOnCartIcon(1);

        CartPage cart = inventory.goToCart();
        assertEquals(1, cart.getItemCount());

        cart.removeProduct(BIKE_LIGHT);
        assertEquals(0, cart.getItemCount());
        assertEquals(ZERO, cart.getCartBadgeCount());
    }

    private void checkItemCountOnCartPage(int expectedCount) {
        CartPage cart = inventory.goToCart();
        assertEquals(expectedCount, cart.getItemCount());
        inventory = cart.continueShopping();
    }

    private void checkItemCountOnCartIcon(int expectedCount) {
        assertEquals(String.valueOf(expectedCount), inventory.getCartBadgeCount());
    }
}
