// 
package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Designation Master
 */
public class DesignationmasterPage extends BasePage {

    // ── Constructor ──────────────────────────────────────────
    public DesignationmasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Locators ─────────────────────────────────────────────
    private final By addDesignationBtn =
            By.xpath("//button[contains(., 'Add Designation')]");

    private final By designationNameField =
            By.cssSelector("input[formcontrolname='designationName']");

    private final By companyDropdown = By.id("companyId");

    private final By submitBtn =
            By.xpath("//button[normalize-space()='Submit']");

    private final By tableRows = By.cssSelector("table tbody tr");

    private final By lastRowDeleteBtn =
            By.cssSelector("table tbody tr:last-child button, table tbody tr:last-child [class*='delete'], table tbody tr:last-child a");

    private final By confirmYesBtn =
            By.xpath("//button[contains(text(),'Yes') or contains(text(),'OK') or contains(text(),'Confirm')]");

    // ── Navigation ───────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/designation-master");
    }

    // ── Actions ──────────────────────────────────────────────

    public void clickAddDesignation() {
        waitClickable(addDesignationBtn).click();
        pause(1500); // modal animation
    }

    public void fillDesignationForm(String name) {
        WebElement field = waitVisible(designationNameField);
        field.clear();
        field.sendKeys(name);
        selectFirstCompany();
    }

    public void selectCompany(String companyName) {
        Select select = new Select(waitVisible(companyDropdown));
        select.selectByVisibleText(companyName);
    }

    /** Picks the first non-placeholder option in the company dropdown. */
    private void selectFirstCompany() {
        Select select = new Select(waitVisible(companyDropdown));
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            String disabled = option.getAttribute("disabled");
            if (disabled == null && option.getAttribute("value") != null
                    && !option.getAttribute("value").equals("null")
                    && !option.getAttribute("value").isEmpty()) {
                select.selectByValue(option.getAttribute("value"));
                return;
            }
        }
    }

    public void submitForm() {
        jsClick(submitBtn);
        pause(2000);
    }

    // ── Verification ─────────────────────────────────────────

    public boolean isDesignationAdded() {
        return isTextPresent("success") || isTextPresent("added");
    }

    public boolean isBlankErrorShown() {
        return isTextPresent("required") || isTextPresent("error");
    }

    public boolean isAddSuccessful() {
        return isTextPresent("success") || isTextPresent("added");
    }

    /** Overloaded method (kept for compatibility) */
    public void FillDesignationForm(String designationName, String extra) {
        fillDesignationForm(designationName);
    }

    public boolean isDuplicateErrorShown() {
        pause(1500);
        return isTextPresent("exists")
            || isTextPresent("duplicate")
            || isTextPresent("already")
            || isTextPresent("same name")
            || isTextPresent("taken");
    }

    // ── Delete ───────────────────────────────────────────────
    public void deleteLastDesignation() {

        org.openqa.selenium.support.ui.WebDriverWait shortWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));

        try {
            shortWait.until(d -> !d.findElements(tableRows).isEmpty());
        } catch (Exception ignored) {
            return; // no data
        }

        try {
            waitClickable(lastRowDeleteBtn).click();

            try {
                waitClickable(confirmYesBtn).click();
            } catch (Exception ignored) {}

        } catch (Exception ignored) {}
    }
}