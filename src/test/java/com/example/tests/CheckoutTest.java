package com.example.tests;

import com.example.core.*;
import com.example.pages.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Shopping Cart & Checkout")
public class CheckoutTest extends BaseTest {

    private void loginAsStandard() {
        new LoginPage(DriverFactory.getDriver()).loginAs(Constants.STANDARD_USER, Constants.PASSWORD);
    }

    @Story("Add to cart")
    @Test(description = "Adding items updates cart badge count")
    public void addingItemsUpdatesBadgeCount() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        Assert.assertTrue(inventory.isCartBadgeAbsent(), "Badge should be absent initially");
        inventory.addBackpack();
        Assert.assertEquals(inventory.cartCount(), "1");
        inventory.addBikeLight();
        Assert.assertEquals(inventory.cartCount(), "2");
    }

    @Story("Add to cart")
    @Test(description = "Removing an item from inventory decrements cart badge")
    public void removingItemDecrementsBadge() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.addBikeLight();
        Assert.assertEquals(inventory.cartCount(), "2");
        inventory.removeBackpack();
        Assert.assertEquals(inventory.cartCount(), "1");
    }

    @Story("Cart")
    @Test(description = "Cart page shows correct items and their names")
    public void cartPageShowsCorrectItems() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.addBikeLight();
        inventory.openCart();

        CartPage cart = new CartPage(DriverFactory.getDriver());
        Assert.assertTrue(cart.isLoaded());
        Assert.assertEquals(cart.getItemCount(), 2);
        Assert.assertTrue(cart.getItemNames().contains("Sauce Labs Backpack"));
        Assert.assertTrue(cart.getItemNames().contains("Sauce Labs Bike Light"));
    }

    @Story("Cart")
    @Test(description = "Remove item from cart page reduces item count")
    public void removeItemFromCartReducesCount() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.addBikeLight();
        inventory.openCart();

        CartPage cart = new CartPage(DriverFactory.getDriver());
        cart.removeItem("sauce-labs-backpack");
        Assert.assertEquals(cart.getItemCount(), 1);
        Assert.assertFalse(cart.getItemNames().contains("Sauce Labs Backpack"));
    }

    @Story("Cart")
    @Test(description = "Continue Shopping button returns to inventory page")
    public void continueShoppingReturnsToInventory() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.openCart();
        new CartPage(DriverFactory.getDriver()).continueShopping();
        Assert.assertTrue(new InventoryPage(DriverFactory.getDriver()).isLoaded());
    }

    @Story("Checkout happy path")
    @Test(description = "Full checkout flow completes with success message")
    public void fullCheckoutFlowSucceeds() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.addBikeLight();
        inventory.openCart();

        CartPage cart = new CartPage(DriverFactory.getDriver());
        Assert.assertEquals(cart.getItemCount(), 2);
        cart.clickCheckout();

        CheckoutPage checkout = new CheckoutPage(DriverFactory.getDriver());
        checkout.fillInfo("QA", "Candidate", "700000");

        String subtotal = checkout.getSubtotal();
        String tax = checkout.getTax();
        String total = checkout.getTotal();
        Assert.assertTrue(subtotal.contains("Item total"), "Subtotal label missing: " + subtotal);
        Assert.assertTrue(tax.contains("Tax"), "Tax label missing: " + tax);
        Assert.assertTrue(total.contains("Total"), "Total label missing: " + total);

        checkout.finish();
        Assert.assertEquals(checkout.completeHeader(), "Thank you for your order!");
    }

    @Story("Checkout form validation")
    @Test(description = "Checkout with all empty fields shows First Name required error")
    public void checkoutEmptyFormShowsFirstNameError() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.openCart();
        new CartPage(DriverFactory.getDriver()).clickCheckout();
        CheckoutPage checkout = new CheckoutPage(DriverFactory.getDriver());
        checkout.fillInfo("", "", "");
        Assert.assertTrue(checkout.getError().contains("First Name is required"));
    }

    @Story("Checkout form validation")
    @Test(description = "Checkout with missing last name shows Last Name required error")
    public void checkoutMissingLastNameShowsError() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.openCart();
        new CartPage(DriverFactory.getDriver()).clickCheckout();
        CheckoutPage checkout = new CheckoutPage(DriverFactory.getDriver());
        checkout.fillInfo("QA", "", "");
        Assert.assertTrue(checkout.getError().contains("Last Name is required"));
    }

    @Story("Checkout form validation")
    @Test(description = "Checkout with missing postal code shows Postal Code required error")
    public void checkoutMissingPostalCodeShowsError() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.addBackpack();
        inventory.openCart();
        new CartPage(DriverFactory.getDriver()).clickCheckout();
        CheckoutPage checkout = new CheckoutPage(DriverFactory.getDriver());
        checkout.fillInfo("QA", "Candidate", "");
        Assert.assertTrue(checkout.getError().contains("Postal Code is required"));
    }

    @Story("Product sorting")
    @Test(description = "Products sort A-Z by default, Z-A reverses order")
    public void productSortingWorks() {
        loginAsStandard();
        InventoryPage inventory = new InventoryPage(DriverFactory.getDriver());
        inventory.sortBy("az");
        String firstAZ = inventory.getItemNames().get(0);
        inventory.sortBy("za");
        String firstZA = inventory.getItemNames().get(0);
        Assert.assertNotEquals(firstAZ, firstZA, "Sort order should differ between A-Z and Z-A");
    }
}
