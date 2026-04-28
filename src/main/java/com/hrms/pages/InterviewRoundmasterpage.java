package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Interview Round Master
 */
public class InterviewRoundmasterpage extends BasePage {

    // ── Locators ─────────────────────────────────────────────
    private final By addInterviewRoundBtn = By.xpath("//button[contains(text(),'Add Interview Timeline Master')]");
    private final By timelineNameField    = By.id("timelineName");
    private final By isActiveCheckbox     = By.id("isActive");
    private final By submitBtn            = By.cssSelector("button[type='submit']");

    // ── Constructor ──────────────────────────────────────────
    public InterviewRoundmasterpage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Navigation ───────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/interview_timeline_master");
        pause(2000);
    }

    // ── Actions ──────────────────────────────────────────────
    public void clickAddInterviewRoundMaster() {
        waitClickable(addInterviewRoundBtn).click();
        pause(1500); // modal animation
    }

    public void fillInterviewRoundForm(String roundName, boolean active) {
        waitVisible(timelineNameField).sendKeys(roundName);
        if (active) {
            waitClickable(isActiveCheckbox).click();
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

    public boolean isBlankErrorShown() {
        return isTextPresent("required") || isTextPresent("error") || isTextPresent("please");
    }
}
