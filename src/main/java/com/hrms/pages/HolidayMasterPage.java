package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Holiday Master.
 */
public class HolidayMasterPage extends BasePage {

    public HolidayMasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Locators ─────────────────────────────────────────────
    private final By addHolidayBtn =
            By.xpath("//button[contains(., 'Add Holiday')]");

    /** Modal-only company dropdown (the page-level filter has class form-select and no id). */
    private final By modalCompanyDropdown = By.id("companyId");

    private final By holidayNameField = By.id("holidayName");

    private final By holidayDateField = By.id("holidayDate");

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
        driver.get(baseUrl + "/#/master-management/holiday-master");
    }

    // ── Actions ──────────────────────────────────────────────
    public void clickAddHoliday() {
        waitClickable(addHolidayBtn).click();
        pause(1500); // modal animation
    }

    /** Fills the modal: selects a company, types the holiday name. Date is pre-filled by the app. */
    public void fillHolidayForm(String holidayName) {
        selectFirstCompany();

        WebElement nameEl = waitVisible(holidayNameField);
        nameEl.clear();
        nameEl.sendKeys(holidayName);
    }

    /** Optional: set an explicit date (YYYY-MM-DD format expected by <input type="date">). */
    public void setHolidayDate(String yyyyMmDd) {
        WebElement dateEl = waitVisible(holidayDateField);
        js.executeScript("arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input'));" +
                "arguments[0].dispatchEvent(new Event('change'));", dateEl, yyyyMmDd);
    }

    private void selectFirstCompany() {
        Select select = new Select(waitVisible(modalCompanyDropdown));
        List<WebElement> options = select.getOptions();
        for (WebElement option : options) {
            String disabled = option.getAttribute("disabled");
            String value = option.getAttribute("value");
            if (disabled == null && value != null && !value.equals("null") && !value.isEmpty()) {
                select.selectByValue(value);
                return;
            }
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
    public void deleteLastHoliday() {
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
