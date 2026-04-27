package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.InterviewModePage;
import com.hrms.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InterviewModeTest extends BaseTest {

    private InterviewModePage interviewModePage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {

        // Always navigate to login and authenticate
        LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
        networkUtils.navigateSafely(baseUrl + "/#/auth/login");

        // Only login if we're actually on the login page
        if (driver.getCurrentUrl().contains("auth/login")) {
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        interviewModePage = new InterviewModePage(driver, ConfigReader.getExplicitWait());
    }

    // ─────────────────────────────────────────────────────────────

    @Test(
    priority = 1,
    description = "Adding a new interview mode with valid data should succeed"
)
public void testAddInterviewMode() throws InterruptedException {

    System.out.println("\n========== InterviewMode: Add Interview Mode ==========");

    // Step 1: Open Interview Mode Page
    interviewModePage.open(baseUrl);

    // Step 2: Click on Add Interview Mode button
    interviewModePage.clickAddInterviewMode();

    // Step 3: Fill the form with valid data
    String interviewName = "TestInterview_" + System.currentTimeMillis();
    String interviewType = "Virtual";

    interviewModePage.fillInterviewForm(interviewName, interviewType);

    // Step 4: Submit the form
    interviewModePage.submitForm();

    // Step 5: Verify success message
    boolean isAdded = interviewModePage.isAddInterviewModePage();

    Assert.assertTrue(
        isAdded,
        "❌ Failed: Interview Mode was not added successfully"
    );

    System.out.println("✅ PASS: Interview Mode Added Successfully");
}


// ─────────────────────────────────────────────────────────────


@Test(
    priority = 2,
    description = "Verify blank validation for interview mode form"
)
public void testBlankInterviewModeValidation() throws InterruptedException {

    System.out.println("\n========== InterviewMode: Blank Validation ==========");

    // Step 1: Open Interview Mode Page
    interviewModePage.open(baseUrl);

    // Step 2: Click on Add Interview Mode button
    interviewModePage.clickAddInterviewMode();

    // Step 3: Submit form without entering data
    interviewModePage.fillInterviewForm("", "");
    interviewModePage.submitForm();

    // Step 4: Verify error message is displayed
    boolean isErrorShown = interviewModePage.isBlankErrorShown();
    System.out.println("Is Blank Error Displayed: " + isErrorShown);

    Assert.assertTrue(
        isErrorShown,
        "❌ Failed: Blank validation message not displayed"
    );

    System.out.println("✅ PASS: Blank Validation Working Correctly");
}
}
