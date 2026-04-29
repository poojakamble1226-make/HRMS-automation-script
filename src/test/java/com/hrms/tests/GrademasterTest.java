package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.GrademasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Grade Master module.
 */
public class GrademasterTest extends BaseTest {

    private GrademasterPage gradePage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        gradePage = new GrademasterPage(driver, ConfigReader.getExplicitWait());
    }

    @Test(priority = 1, description = "Adding a new grade with valid data should succeed")
    public void testAddGrade() throws InterruptedException {
        System.out.println("\n---------- GRADE MASTER: Add Grade ----------");
        gradePage.open(baseUrl);

        gradePage.clickAddGrade();
        gradePage.fillGradeForm(
            "TestGrade_" + System.currentTimeMillis(),
            "Auto-created by Selenium suite"
        );
        gradePage.submitForm();

        Assert.assertTrue(
            gradePage.isAddSuccessful(),
            "Success message should appear after adding a grade"
        );
        System.out.println("PASS: Grade Added");
    }

    @Test(priority = 2, description = "Adding a grade that already exists should show a duplicate error")
    public void testDuplicateGradeValidation() throws InterruptedException {
        System.out.println("\n---------- GRADE MASTER: Duplicate Validation ----------");
        String dupName = "DupGrade_" + System.currentTimeMillis();

        // First insert: should succeed and guarantees the name now exists.
        gradePage.open(baseUrl);
        gradePage.clickAddGrade();
        gradePage.fillGradeForm(dupName, "Auto-created");
        gradePage.submitForm();

        // Second insert with the same name: should trigger duplicate validation.
        gradePage.open(baseUrl);
        gradePage.clickAddGrade();
        gradePage.fillGradeForm(dupName, "Auto-created");
        gradePage.submitForm();

        Assert.assertTrue(
            gradePage.isDuplicateErrorShown(),
            "Duplicate-Grade error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last grade in the list should complete without error")
    public void testDeleteGrade() throws InterruptedException {
        System.out.println("\n---------- GRADE MASTER: Delete Grade ----------");
        gradePage.open(baseUrl);

        gradePage.deleteLastGrade();

        System.out.println("PASS: Delete Action Performed");
    }
}
