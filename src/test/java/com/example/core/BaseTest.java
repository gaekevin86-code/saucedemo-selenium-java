package com.example.core;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.time.Duration;

public class BaseTest {
    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
        DriverFactory.createDriver(browser);
        // Retry page load up to 3 times to handle transient DNS/network errors
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                DriverFactory.getDriver().get(Constants.BASE_URL);
                new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
                        .until(ExpectedConditions.urlContains("saucedemo.com"));
                return;
            } catch (Exception e) {
                if (attempt == 3) throw e;
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
