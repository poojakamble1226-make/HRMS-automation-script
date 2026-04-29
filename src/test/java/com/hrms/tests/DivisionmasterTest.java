package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.DivisionmasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Division Master module.
 */
public class DivisionmasterTest extends BaseTest {

    private DivisionmasterPage divisionPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        divisionPage = new DivisionmasterPage(driver, ConfigReader.getExplicitWait());
    }

    @Test(priority = 1, description = "Adding a new division with valid data should succeed")
    public void testAddDivision() throws InterruptedException {
        System.out.println("\n---------- DIVISION MASTER: Add Division ----------");
        divisionPage.open(baseUrl);

        divisionPage.clickAddDivision();
        divisionPage.fillDivisionForm(
            "ghhhgh" + System.currentTimeMillis(),
            "vbgfffff"
        );
        divisionPage.submitForm();

        Assert.assertTrue(
            divisionPage.isAddSuccessful(),
            "Success message should appear after adding a division"
        );
        System.out.println("PASS: Division Added");
    }

    @Test(priority = 2, description = "Adding a division that already exists should show a duplicate error")
    public void testDuplicateDivisionValidation() throws InterruptedException {
        System.out.println("\n---------- DIVISION MASTER: Duplicate Validation ----------");
        String dupName = "DupDiv_" + System.currentTimeMillis();

        // First insert: should succeed and guarantees the name now exists.
        divisionPage.open(baseUrl);
        divisionPage.clickAddDivision();
        divisionPage.fillDivisionForm(dupName, "vbgfffff");
        divisionPage.submitForm();

        // Second insert with the same name: should trigger duplicate validation.
        divisionPage.open(baseUrl);
        divisionPage.clickAddDivision();
        divisionPage.fillDivisionForm(dupName, "vbgfffff");
        divisionPage.submitForm();

        Assert.assertTrue(
            divisionPage.isDuplicateErrorShown(),
            "Duplicate-Division error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last division in the list should complete without error")
    public void testDeleteDivision() throws InterruptedException {
        System.out.println("\n---------- DIVISION MASTER: Delete Division ----------");
        divisionPage.open(baseUrl);

        divisionPage.deleteLastDivision();

        System.out.println("PASS: Delete Action Performed");
    }
}
