package com.example.tests;

import com.example.core.*;
import com.example.pages.*;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.*;

@Feature("Login")
public class LoginTest extends BaseTest {

    @Story("Valid login")
    @Test(description = "Standard user logs in and lands on inventory page")
    public void validLoginShouldOpenInventory() {
        new LoginPage(DriverFactory.getDriver()).loginAs(Constants.STANDARD_USER, Constants.PASSWORD);
        Assert.assertTrue(new InventoryPage(DriverFactory.getDriver()).isLoaded());
    }

    @Story("Valid login")
    @Test(description = "Performance glitch user eventually logs in successfully")
    public void performanceGlitchUserCanLogin() {
        new LoginPage(DriverFactory.getDriver()).loginAs(Constants.PERF_GLITCH_USER, Constants.PASSWORD);
        Assert.assertTrue(new InventoryPage(DriverFactory.getDriver()).isLoaded());
    }

    @DataProvider
    public Object[][] invalidUsers() {
        return new Object[][]{
            {Constants.LOCKED_USER, Constants.PASSWORD, "Sorry, this user has been locked out"},
            {Constants.STANDARD_USER, "wrong_password", "Username and password do not match"},
            {"", Constants.PASSWORD, "Username is required"},
            {Constants.STANDARD_USER, "", "Password is required"},
            {"invalid_user", Constants.PASSWORD, "Username and password do not match"},
        };
    }

    @Story("Invalid login")
    @Test(dataProvider = "invalidUsers",
          description = "Invalid credentials show descriptive error message")
    public void invalidLoginShouldShowError(String user, String pass, String expectedError) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver()).loginAs(user, pass);
        Assert.assertTrue(loginPage.getError().contains(expectedError),
                "Expected error containing: " + expectedError);
    }
}
