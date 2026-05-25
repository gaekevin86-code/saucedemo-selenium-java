package com.example.pages;

import com.example.core.Constants;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By completeHeader = By.className("complete-header");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT));
    }

    @Step("Fill checkout info: firstName='{firstName}', lastName='{lastName}', postalCode='{postalCode}'")
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        wait.until(ExpectedConditions.elementToBeClickable(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
        driver.findElement(continueButton).click();
        return this;
    }

    @Step("Click Finish button to complete order")
    public void finish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    @Step("Complete full checkout")
    public void checkout(String firstName, String lastName, String postalCode) {
        fillInfo(firstName, lastName, postalCode);
        finish();
    }

    @Step("Get order complete header text")
    public String completeHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    @Step("Get error message")
    public String getError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    @Step("Get subtotal label")
    public String getSubtotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel)).getText();
    }

    @Step("Get tax label")
    public String getTax() {
        return driver.findElement(taxLabel).getText();
    }

    @Step("Get total label")
    public String getTotal() {
        return driver.findElement(totalLabel).getText();
    }
}
