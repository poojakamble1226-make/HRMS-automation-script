package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Branch Master screen.
 */
public class BranchMasterPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By addBranchBtn   = By.cssSelector("button.btn-primary");
    private final By branchNameFld  = By.id("branchName");
    private final By addressFld     = By.id("address");
    private final By phoneFld       = By.id("phone");
    private final By emailFld       = By.id("email");
    private final By submitBtn      = By.cssSelector("button[type='submit']");
    private final By submitEnabled  = By.cssSelector("button[type='submit']:not([disabled])");
    private final By branchNameError = By.xpath("//div[contains(text(),'valid branch name')]");
    private final By phoneError = By.xpath("//div[contains(text(),'10 digit')]");

    public BranchMasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/branch-master");
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void clickAddBranch() {
        waitClickable(addBranchBtn).click();
        waitVisible(branchNameFld);
        pause(800); // allow Angular animations to settle
    }

    /**
     * Touches each required field without filling it, then clicks Submit.
     * This triggers Angular's (blur) required-field validators.
     */
    public void triggerEmptyValidation() {
        touchField(branchNameFld);
        touchField(addressFld);
        touchField(phoneFld);
        touchField(emailFld);
        jsClick(submitBtn);
        pause(1000);
    }

    public void fillBranchForm(String name, String address, String phone, String email) {
        type(branchNameFld, name);
        type(addressFld,    address);
        type(phoneFld,      phone);
        type(emailFld,      email);
        pause(500);
    }

    public void submitForm() {
        // Don't block on the button being enabled — invalid-input tests
        // intentionally leave it disabled and just want validation to render.
        org.openqa.selenium.support.ui.WebDriverWait shortWait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3));
        try {
            shortWait.until(d -> !d.findElements(submitEnabled).isEmpty());
        } catch (Exception ignored) {}
        jsClick(submitBtn);
        pause(1500);
    }

    // ── Verification ──────────────────────────────────────────────────────────

    public boolean isEmptyValidationShown() {
        return isTextPresent("required");
    }
    public boolean isBranchNameValidationShown() {
    return isElementDisplayed(branchNameError);
}

// Check Phone validation
public boolean isPhoneValidationShown() {
    return isElementDisplayed(phoneError);
}
}
