package com.hrms.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import java.time.Duration;

/**
 * Base class for all Page Objects.
 * Provides reusable low-level interactions so individual pages
 * never duplicate wait/click/type logic.
 */
public class BasePage {

    protected final WebDriver        driver;
    protected final WebDriverWait    wait;
    protected final JavascriptExecutor js;

    public BasePage(WebDriver driver, int waitSeconds) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
        this.js     = (JavascriptExecutor) driver;
    }

    // ── Waits ────────────────────────────────────────────────────────────────

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ── Interactions ─────────────────────────────────────────────────────────
    protected void click(By locator) {
        waitClickable(locator).click();
    }
    /**
     * Scrolls to the element then clicks it via JavaScript.
     * Useful when overlays or Angular's change-detection block a normal click.
     */
    protected void jsClick(By locator) {
        WebElement el = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", el);
        js.executeScript("arguments[0].click();", el);
    }

    /** Clears the field then types the given value. */
    protected void type(By locator, String value) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(value);
    }

    /**
     * Focuses a field and presses TAB to trigger Angular's (blur) validation.
     * Silently ignores elements that are not currently present.
     */
    protected void touchField(By locator) {
        try {
            WebElement el = driver.findElement(locator);
            el.click();
            el.sendKeys(Keys.TAB);
        } catch (Exception ignored) {}
    }

    // ── Assertions helpers ───────────────────────────────────────────────────

    protected boolean isTextPresent(String text) {
        return driver.getPageSource().toLowerCase().contains(text.toLowerCase());
    }
    // ── Assertions helpers ───────────────────────────────────────────────────

protected boolean isDisplayed(By locator) {
    try {
        return waitVisible(locator).isDisplayed();
    } catch (Exception e) {
        return false;
    }
}

    // ── Utility ──────────────────────────────────────────────────────────────

    protected void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
    public boolean isElementDisplayed(By locator) {
    try {
        return driver.findElement(locator).isDisplayed();
    } catch (NoSuchElementException e) {
        return false;
    }
}
}
