package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.DesignationmasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Company Master module.
 */
public class DesignationmasterTest extends BaseTest {

    private DesignationmasterPage Designationpage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        // Login once before any company test runs (skip if already logged in)
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        Designationpage = new DesignationmasterPage(driver, ConfigReader.getExplicitWait());
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Adding a new Designation with valid data should succeed")
    public void testAdddesignation() throws InterruptedException {
        System.out.println("\n---------- DESIGNATION MASTER: Add DESIGNATION ----------");
        Designationpage.open(baseUrl);

       Designationpage.clickAddDesignation();
        Designationpage.FillDesignationForm(
            "TestDesignation_" + System.currentTimeMillis(),
            "Test SelectDesignation test"
        );
        Designationpage.submitForm();

        Assert.assertTrue(
            Designationpage.isAddSuccessful(),
            "Success message should appear after adding a Designation"
        );
        System.out.println("PASS: Designation Added");
    }

    @Test(priority = 2, description = "Adding a Designation that already exists should show a duplicate error")
    public void testDuplicateDesignationValidation() throws InterruptedException {
        System.out.println("\n---------- DESIGNATION MASTER: Duplicate Validation ----------");
       Designationpage.open(baseUrl);

       Designationpage.clickAddDesignation();
        Designationpage.fillDesignationForm("Qa Annayst.");
        Designationpage.submitForm();

        Assert.assertTrue(
            Designationpage.isDuplicateErrorShown(),
            "Duplicate-Designation error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last Designation in the list should complete without error")
    public void testDeleteCompany() throws InterruptedException {
        System.out.println("\n---------- Designation MASTER: Delete Designation ----------");
        Designationpage.open(baseUrl);

        Designationpage.deleteLastDesignation();

        System.out.println("PASS: Delete Action Performed");
    }
}
