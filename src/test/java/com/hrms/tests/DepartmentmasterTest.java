package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.DepartmentmasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Department Master module.
 */
public class DepartmentmasterTest extends BaseTest {

    private DepartmentmasterPage departmentPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        departmentPage = new DepartmentmasterPage(driver, ConfigReader.getExplicitWait());
    }

    @Test(priority = 1, description = "Adding a new department with valid data should succeed")
    public void testAddDepartment() throws InterruptedException {
        System.out.println("\n---------- DEPARTMENT MASTER: Add Department ----------");
        departmentPage.open(baseUrl);

        departmentPage.clickAddDepartment();
        departmentPage.fillDepartmentForm(
            "TestDept_" + System.currentTimeMillis(),
            "Auto-created by Selenium suite"
        );
        departmentPage.submitForm();

        Assert.assertTrue(
            departmentPage.isAddSuccessful(),
            "Success message should appear after adding a department"
        );
        System.out.println("PASS: Department Added");
    }

    @Test(priority = 2, description = "Adding a department that already exists should show a duplicate error")
    public void testDuplicateDepartmentValidation() throws InterruptedException {
        System.out.println("\n---------- DEPARTMENT MASTER: Duplicate Validation ----------");
        departmentPage.open(baseUrl);

        departmentPage.clickAddDepartment();
        departmentPage.fillDepartmentForm("HR", "Duplicate test");
        departmentPage.submitForm();

        Assert.assertTrue(
            departmentPage.isDuplicateErrorShown(),
            "Duplicate-Department error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last department in the list should complete without error")
    public void testDeleteDepartment() throws InterruptedException {
        System.out.println("\n---------- DEPARTMENT MASTER: Delete Department ----------");
        departmentPage.open(baseUrl);

        departmentPage.deleteLastDepartment();

        System.out.println("PASS: Delete Action Performed");
    }
}
