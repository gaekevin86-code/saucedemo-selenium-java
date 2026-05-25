# SauceDemo Selenium Java Test Suite

## Tech Stack
- Java 17, Maven, Selenium 4.27, TestNG 7.10, Allure 2.27
- WebDriverManager (auto-manages ChromeDriver / GeckoDriver)
- Page Object Model + ThreadLocal for parallel safety

## Test Coverage

### Login (`LoginTest`)
| Test | Type |
|---|---|
| Standard user logs in → inventory page | Happy path |
| Performance glitch user logs in (slow but succeeds) | Edge case |
| Locked-out user → error message | Edge case |
| Wrong password → error message | Edge case |
| Empty username → "Username is required" | Edge case |
| Empty password → "Password is required" | Edge case |
| Unknown user → error message | Edge case |

### Cart & Checkout (`CheckoutTest`)
| Test | Type |
|---|---|
| Adding items updates cart badge count | Happy path |
| Removing item decrements badge | Edge case |
| Cart page shows correct item names | Happy path |
| Remove item from cart page | Edge case |
| Continue Shopping returns to inventory | Edge case |
| Full checkout flow → "Thank you for your order!" | Happy path |
| Verify subtotal / tax / total labels on overview | Verification |
| Empty checkout form → "First Name is required" | Edge case |
| Missing last name → "Last Name is required" | Edge case |
| Missing postal code → "Postal Code is required" | Edge case |
| Product sort A-Z vs Z-A | Edge case |

## Project Structure
```
src/test/java/com/example/
├── core/
│   ├── Constants.java          # Credentials, URLs, timeouts
│   ├── BaseTest.java           # @BeforeMethod / @AfterMethod
│   ├── DriverFactory.java      # ThreadLocal WebDriver (Chrome & Firefox headless)
│   └── ScreenshotListener.java # Auto-screenshot on failure → target/screenshots/
├── pages/
│   ├── LoginPage.java
│   ├── InventoryPage.java
│   ├── CartPage.java
│   └── CheckoutPage.java
└── tests/
    ├── LoginTest.java
    └── CheckoutTest.java
```

## Run Locally

**Prerequisites:** Java 17+, Maven 3.8+, Chrome and Firefox installed.

```bash
# Run full suite (Chrome + Firefox in parallel)
mvn test

# Run on one browser only
mvn test -Dbrowser=chrome

# Generate & open Allure report
mvn test
allure serve target/allure-results
```

## CI / GitHub Actions
Push or open a PR → tests run automatically on `ubuntu-latest`.
- Allure HTML report is uploaded as artifact `allure-report`
- Screenshots are uploaded as artifact `failure-screenshots` on failure
