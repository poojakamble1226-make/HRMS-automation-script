package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Grade Master.
 */
public class GrademasterPage extends BasePage {

    public GrademasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Locators ─────────────────────────────────────────────
    private final By addGradeBtn =
            By.xpath("//button[contains(., 'Add Grade')]");

    private final By gradeNameField = By.id("gradeName");

    private final By descriptionField = By.id("description");

    private final By submitBtn =
            By.cssSelector("button[type='submit']");

    private final By tableRows = By.cssSelector("table tbody tr");

    private final By lastRowActionMenu =
            By.cssSelector("table tbody tr:last-child .fa-ellipsis-vertical, table tbody tr:last-child [class*='ellipsis']");

    private final By deleteMenuItem =
            By.cssSelector(".fa-trash, [class*='trash'], [class*='delete']");

    private final By confirmYesBtn =
            By.xpath("//button[contains(text(),'Yes') or contains(text(),'OK') or contains(text(),'Confirm')]");

    // ── Navigation ───────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/grade-master");
    }

    // ── Actions ──────────────────────────────────────────────
    public void clickAddGrade() {
        waitClickable(addGradeBtn).click();
        pause(1500); // modal animation
    }

    public void fillGradeForm(String name, String description) {
        WebElement nameEl = waitVisible(gradeNameField);
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
    public void deleteLastGrade() {
        org.openqa.selenium.support.ui.WebDriverWait shortWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        try {
            shortWait.until(d -> !d.findElements(tableRows).isEmpty());
        } catch (Exception ignored) {
            return;
        }
        try {
            waitClickable(lastRowActionMenu).click();
            pause(500);
            try {
                waitClickable(deleteMenuItem).click();
            } catch (Exception ignored) {}
            try {
                waitClickable(confirmYesBtn).click();
            } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }
}
