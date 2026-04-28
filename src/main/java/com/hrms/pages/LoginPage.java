package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Login screen.
 */
public class LoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By submitBtn     = By.cssSelector("button.submit-btn");

    public LoginPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/auth/login");
    }

    public void enterUsername(String username) {
        waitVisible(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(submitBtn).click();
    }

    /** Convenience: fill both fields and click Login in one call. */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // ── Verification ──────────────────────────────────────────────────────────

    /** Waits until the URL contains "dashboard" and returns true if it does. */
    public boolean isDashboardLoaded() {
        wait.until(ExpectedConditions.urlContains("dashboard"));
        return driver.getCurrentUrl().contains("dashboard");
    }
}
