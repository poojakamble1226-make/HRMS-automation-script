package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.CompanyMasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Company Master module.
 */
public class CompanyMasterTest extends BaseTest {

    private CompanyMasterPage companyPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        // Login once before any company test runs (skip if already logged in)
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        companyPage = new CompanyMasterPage(driver, ConfigReader.getExplicitWait());
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Adding a new company with valid data should succeed")
    public void testAddCompany() throws InterruptedException {
        System.out.println("\n---------- COMPANY MASTER: Add Company ----------");
        companyPage.open(baseUrl);

        companyPage.clickAddCompany();
        companyPage.fillCompanyForm(
            "TestCompany_" + System.currentTimeMillis(),
            "Test Footer Text"
        );
        companyPage.submitForm();

        Assert.assertTrue(
            companyPage.isAddSuccessful(),
            "Success message should appear after adding a company"
        );
        System.out.println("PASS: Company Added");
    }

    @Test(priority = 2, description = "Adding a company that already exists should show a duplicate error")
    public void testDuplicateCompanyValidation() throws InterruptedException {
        System.out.println("\n---------- COMPANY MASTER: Duplicate Validation ----------");
        companyPage.open(baseUrl);

        companyPage.clickAddCompany();
        companyPage.fillCompanyForm("Demo Solutions Pvt. Ltd.", null);
        companyPage.submitForm();

        Assert.assertTrue(
            companyPage.isDuplicateErrorShown(),
            "Duplicate-company error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last company in the list should complete without error")
    public void testDeleteCompany() throws InterruptedException {
        System.out.println("\n---------- COMPANY MASTER: Delete Company ----------");
        companyPage.open(baseUrl);

        companyPage.deleteLastCompany();

        System.out.println("PASS: Delete Action Performed");
    }
}
