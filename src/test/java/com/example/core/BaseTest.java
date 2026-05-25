package com.example.core;

import org.testng.annotations.*;

public class BaseTest {
    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) {
        DriverFactory.createDriver(browser);
        DriverFactory.getDriver().get(Constants.BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
