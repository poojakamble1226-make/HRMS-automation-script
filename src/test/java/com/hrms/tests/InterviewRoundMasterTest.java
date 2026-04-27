package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.InterviewRoundmasterpage;
import com.hrms.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InterviewRoundMasterTest extends BaseTest {

    private InterviewRoundmasterpage interviewRoundPage;

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

        interviewRoundPage = new InterviewRoundmasterpage(driver, ConfigReader.getExplicitWait());
    }

    // ─────────────────────────────────────────────────────────────

    @Test(
        priority = 1,
        description = "Adding a new interview timeline with valid data should succeed"
    )
    public void testAddInterviewRound() throws InterruptedException {

        System.out.println("\n========== InterviewRoundMaster: Add Timeline ==========");

        // Step 1: Open Interview Round Master Page
        interviewRoundPage.open(baseUrl);

        // Step 2: Click on Add Interview Timeline Master button
        interviewRoundPage.clickAddInterviewRoundMaster();

        // Step 3: Fill the form with valid data
        String timelineName = "TestTimeline_" + System.currentTimeMillis();
        interviewRoundPage.fillInterviewRoundForm(timelineName, true);

        // Step 4: Submit the form
        interviewRoundPage.submitForm();

        // Step 5: Verify success message
        Assert.assertTrue(
            interviewRoundPage.isAddSuccessful(),
            "Failed: Interview Timeline was not added successfully"
        );

        System.out.println("PASS: Interview Timeline Added Successfully");
    }

    // ─────────────────────────────────────────────────────────────

    @Test(
        priority = 2,
        description = "Verify blank validation for interview timeline form"
    )
    public void testBlankInterviewRoundValidation() throws InterruptedException {

        System.out.println("\n========== InterviewRoundMaster: Blank Validation ==========");

        // Step 1: Open Interview Round Master Page
        interviewRoundPage.open(baseUrl);

        // Step 2: Click on Add Interview Timeline Master button
        interviewRoundPage.clickAddInterviewRoundMaster();

        // Step 3: Submit form without entering data
        interviewRoundPage.fillInterviewRoundForm("", false);
        interviewRoundPage.submitForm();

        // Step 4: Verify error message is displayed
        Assert.assertTrue(
            interviewRoundPage.isBlankErrorShown(),
            "Failed: Blank validation message not displayed"
        );

        System.out.println("PASS: Blank Validation Working Correctly");
    }
}
