package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Interview Mode Master
 */
public class InterviewModePage extends BasePage {

    // ── Locators ─────────────────────────────────────────────
    private final By addInterviewBtn    = By.xpath("//button[contains(text(),'Add Interview Mode')]");
    private final By interviewModeField = By.cssSelector("input[placeholder='Enter Interview Mode Here']");
    private final By descField          = By.cssSelector("input[placeholder='Enter Description Here']");
    private final By submitBtn          = By.cssSelector("button[type='submit']");

    // ── Constructor ──────────────────────────────────────────
    public InterviewModePage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Navigation ───────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/interview_mode-master");
        pause(2000);
    }

    // ── Actions ──────────────────────────────────────────────
    public void clickAddInterviewMode() {
        waitClickable(addInterviewBtn).click();
        pause(1500); // modal animation
    }

    public void fillInterviewForm(String mode, String description) {
        waitVisible(interviewModeField).sendKeys(mode);
        if (description != null) {
            waitVisible(descField).sendKeys(description);
        }
    }

    public void submitForm() {
        jsClick(submitBtn);
        pause(2000);
    }

    // ── Verification ─────────────────────────────────────────
    public boolean isAddInterviewModePage() {
        return isTextPresent("success") || isTextPresent("added");
    }

    public boolean isBlankErrorShown() {
        return isTextPresent("required") || isTextPresent("error") || isTextPresent("please");
    }
}