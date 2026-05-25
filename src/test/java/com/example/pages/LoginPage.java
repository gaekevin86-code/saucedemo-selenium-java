package com.example.pages;

import com.example.core.Constants;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_TIMEOUT));
    }

    @Step("Login with username='{user}' and password='{pass}'")
    public LoginPage loginAs(String user, String pass) {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(user);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(pass);
        driver.findElement(loginButton).click();
        return this;
    }

    @Step("Get error message")
    public String getError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}
