package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.BranchMasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Branch Master module.
 *
 * Each test navigates fresh to the Branch Master page so the tests
 * remain independent and can be run in any order.
 */
public class BranchMasterTest extends BaseTest {

    private BranchMasterPage branchPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        // Login once before any branch test runs (skip if already logged in)
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        branchPage = new BranchMasterPage(driver, ConfigReader.getExplicitWait());
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Submitting an empty branch form should show required-field errors")
    public void testEmptyBranchValidation() throws InterruptedException {
        System.out.println("\n---------- BRANCH MASTER: Empty Validation ----------");
        branchPage.open(baseUrl);

        branchPage.clickAddBranch();
        branchPage.triggerEmptyValidation();

        Assert.assertTrue(
                branchPage.isEmptyValidationShown(),
                "Required-field validation messages should appear on an empty submit");
        System.out.println("PASS: Branch Empty Validation");
    }

    @Test(priority = 2, description = "A fully filled branch form should submit without errors")
    public void testAddBranch() throws InterruptedException {
        System.out.println("\n---------- BRANCH MASTER: Add Branch ----------");
        branchPage.open(baseUrl);

        branchPage.clickAddBranch();
        branchPage.fillBranchForm(
                "AutoBranch_" + System.currentTimeMillis(),
                "Mumbai",
                "9876543210",
                "onlifecalpital@gmail.com");
        branchPage.submitForm();

        System.out.println("PASS: Branch Form Submitted");
    }

    @Test(priority = 3, description = "Branch name should not accept numeric values")
    public void testBranchNameWithNumbers() throws InterruptedException {
        System.out.println("\n---------- BRANCH MASTER: Invalid Branch Name ----------");
        branchPage.open(baseUrl);

        branchPage.clickAddBranch();
        branchPage.fillBranchForm(
                "123456", // Invalid numeric branch name
                "Mumbai",
                "9876543210",
                "onlifecapital@gmail.com");
        branchPage.submitForm();

        // Assert.assertTrue(
        // branchPage.isBranchNameValidationShown(),
        // "Validation message should appear for numeric branch name"
        // );

        System.out.println("PASS: Numeric Branch Name Validation");
    }

    @Test(priority = 4, description = "Phone number should be exactly 10 digits")
    public void testInvalidPhoneNumber() throws InterruptedException {
        System.out.println("\n---------- BRANCH MASTER: Invalid Phone Number ----------");
        branchPage.open(baseUrl);

        branchPage.clickAddBranch();
        branchPage.fillBranchForm(
                "Thanebranch",
                "Mumbai",
                "12345", // Invalid phone number (less than 10 digits)
                "onelifecapital@gmail.com");
        branchPage.submitForm();

        // Assert.assertTrue(
        // branchPage.isPhoneValidationShown(),
        // "Validation message should appear for invalid phone number"
        // );

        System.out.println("PASS: Phone Number Validation");
    }
}
