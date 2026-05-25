package com.example.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Map;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private static final boolean HEADLESS = Boolean.parseBoolean(
            System.getProperty("headless", System.getenv("CI") != null ? "true" : "false")
    );

    private static final long SLOW_MO = Long.parseLong(
            System.getProperty("slowmo", "0")
    );

    public static WebDriver getDriver() { return DRIVER.get(); }

    public static void createDriver(String browser) {
        int halfW = 960, fullH = 1000;
        if (!HEADLESS) {
            try {
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                halfW = screen.width / 2;
                fullH = screen.height - 80;
            } catch (java.awt.HeadlessException ignored) {}
        }

        WebDriver raw;
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            if (HEADLESS) options.addArguments("-headless");
            raw = new FirefoxDriver(options);
            if (!HEADLESS) {
                raw.manage().window().setSize(new org.openqa.selenium.Dimension(halfW, fullH));
                raw.manage().window().setPosition(new Point(halfW, 0));
            }
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            // Tắt password manager để không hiện popup "Change your password"
            options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false
            ));
            options.addArguments("--disable-save-password-bubble");
            if (HEADLESS) {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1440,900");
            } else {
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            }
            raw = new ChromeDriver(options);
            if (!HEADLESS) {
                raw.manage().window().setSize(new org.openqa.selenium.Dimension(halfW, fullH));
                raw.manage().window().setPosition(new Point(0, 0));
            }
        }

        WebDriver driver = (SLOW_MO > 0)
                ? new EventFiringDecorator<>(new SlowMoListener(SLOW_MO)).decorate(raw)
                : raw;

        DRIVER.set(driver);
    }

    public static void quitDriver() {
        if (getDriver() != null) { getDriver().quit(); DRIVER.remove(); }
    }
}
