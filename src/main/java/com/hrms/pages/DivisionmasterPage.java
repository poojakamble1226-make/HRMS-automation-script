package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Division Master.
 */
public class DivisionmasterPage extends BasePage {

    public DivisionmasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Locators ─────────────────────────────────────────────
    private final By addDivisionBtn =
            By.xpath("//button[contains(., 'Add Division')]");

    private final By divisionNameField = By.id("divisionName");

    private final By descriptionField = By.id("description");

    private final By submitBtn =
            By.cssSelector("button[type='submit']");

    private final By tableRows = By.cssSelector("table tbody tr");

    private final By lastRowDeleteBtn =
            By.cssSelector("table tbody tr:last-child button, table tbody tr:last-child [class*='delete'], table tbody tr:last-child a");

    private final By confirmYesBtn =
            By.xpath("//button[contains(text(),'Yes') or contains(text(),'OK') or contains(text(),'Confirm')]");

    // ── Navigation ───────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/division-master");
    }

    // ── Actions ──────────────────────────────────────────────
    public void clickAddDivision() {
        waitClickable(addDivisionBtn).click();
        pause(1500); // modal animation
    }

    public void fillDivisionForm(String name, String description) {
        WebElement nameEl = waitVisible(divisionNameField);
        nameEl.clear();
        nameEl.sendKeys(name);

        if (description != null) {
            WebElement descEl = waitVisible(descriptionField);
            descEl.clear();
            descEl.sendKeys(description);
        }
    }

    public void submitForm() {
        jsClick(submitBtn);
        pause(2000);
    }

    // ── Verification ─────────────────────────────────────────
    public boolean isAddSuccessful() {
        return isTextPresent("success") || isTextPresent("added");
    }

    public boolean isDuplicateErrorShown() {
        return hasDuplicateOrErrorIndicator();
    }

    // ── Delete ───────────────────────────────────────────────
    public void deleteLastDivision() {
        org.openqa.selenium.support.ui.WebDriverWait shortWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        try {
            shortWait.until(d -> !d.findElements(tableRows).isEmpty());
        } catch (Exception ignored) {
            return;
        }
        try {
            waitClickable(lastRowDeleteBtn).click();
            try {
                waitClickable(confirmYesBtn).click();
            } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }
}
