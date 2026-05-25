package com.example.pages;

import com.example.core.Constants;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT));
    }

    @Step("Verify cart page is loaded")
    public boolean isLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
        return driver.getCurrentUrl().contains("cart.html");
    }

    @Step("Get number of items in cart")
    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    @Step("Get item names in cart")
    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Remove item '{productId}' from cart")
    public void removeItem(String productId) {
        By removeBtn = By.id("remove-" + productId);
        wait.until(ExpectedConditions.elementToBeClickable(removeBtn)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(removeBtn));
    }

    @Step("Click Checkout button")
    public void clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
    }

    @Step("Click Continue Shopping button")
    public void continueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }
}
