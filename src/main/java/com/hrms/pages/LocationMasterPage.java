package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Page Object for Location Master.
 */
public class LocationMasterPage extends BasePage {

    public LocationMasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Locators ─────────────────────────────────────────────
    private final By addLocationBtn =
            By.xpath("//button[contains(., 'Add Location')]");

    private final By locationField = By.id("location");
    private final By stateField    = By.id("state");
    private final By addressField  = By.id("address");
    private final By pincodeField  = By.id("pincode");
    private final By cityField     = By.id("city");
    private final By companyDropdown = By.id("companyId");

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
        driver.get(baseUrl + "/#/master-management/location-master");
        pause(1000); // let Angular start rendering

        // Press ESC twice to dismiss any orphan modal/overlay left by a previous test.
        try {
            driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
            driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
        } catch (Exception ignored) {}

        // Wait until the Add Location button shows up in the DOM (regardless of viewport).
        try {
            wait.until(d -> !d.findElements(addLocationBtn).isEmpty());
        } catch (Exception ignored) {}
    }

    // ── Actions ──────────────────────────────────────────────
    public void clickAddLocation() {
        // Scroll the button into view first — guards against off-viewport clickability quirks.
        try {
            WebElement btn = waitVisible(addLocationBtn);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            pause(300);
        } catch (Exception ignored) {}

        try {
            waitClickable(addLocationBtn).click();
        } catch (Exception e) {
            // Fallback: JS click bypasses any transparent overlay quirks.
            jsClick(addLocationBtn);
        }
        pause(1500); // modal animation
    }

    /**
     * Fills all required fields in the Add Location modal and picks the first company.
     * Pass null for fields you want to skip (won't be cleared/typed), but note that
     * the form requires all six to be present.
     */
    public void fillLocationForm(String location, String state, String address,
                                 String pincode, String city) {
        typeIfNotNull(locationField, location);
        typeIfNotNull(stateField,    state);
        typeIfNotNull(addressField,  address);
        typeIfNotNull(pincodeField,  pincode);
        typeIfNotNull(cityField,     city);
        selectFirstCompany();
    }

    private void typeIfNotNull(By locator, String value) {
        if (value == null) return;
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(value);
    }

    public void selectCompany(String companyName) {
        Select select = new Select(waitVisible(companyDropdown));
        select.selectByVisibleText(companyName);
    }

    private void selectFirstCompany() {
        Select select = new Select(waitVisible(companyDropdown));
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
        return isTextPresent("success") || isTextPresent("added") || isTextPresent("saved");
    }

    public boolean isDuplicateErrorShown() {
        return hasDuplicateOrErrorIndicator();
    }

    // ── Delete ───────────────────────────────────────────────
    public void deleteLastLocation() {
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
