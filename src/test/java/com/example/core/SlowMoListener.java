package com.example.core;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

public class SlowMoListener implements WebDriverListener {
    private final long delayMs;

    public SlowMoListener(long delayMs) {
        this.delayMs = delayMs;
    }

    @Override
    public void afterClick(WebElement element) { sleep(); }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) { sleep(); }

    private void sleep() {
        try { Thread.sleep(delayMs); } catch (InterruptedException ignored) {}
    }
}
