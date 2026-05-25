package com.example.pages;

import com.example.core.Constants;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By sortDropdown = By.className("product_sort_container");
    private final By itemNames = By.className("inventory_item_name");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT));
    }

    public boolean isLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        return driver.getCurrentUrl().contains("inventory.html");
    }

    public void addBackpack() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("remove-sauce-labs-backpack")));
    }

    public void addBikeLight() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-bike-light"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("remove-sauce-labs-bike-light")));
    }

    public void removeBackpack() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("remove-sauce-labs-backpack"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
    }

    public String cartCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    public boolean isCartBadgeAbsent() {
        return driver.findElements(cartBadge).isEmpty();
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    public void sortBy(String value) {
        new Select(wait.until(ExpectedConditions.elementToBeClickable(sortDropdown))).selectByValue(value);
    }

    public List<String> getItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
