package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.HolidayMasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Holiday Master module.
 */
public class HolidayMasterTest extends BaseTest {

    private HolidayMasterPage holidayPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        holidayPage = new HolidayMasterPage(driver, ConfigReader.getExplicitWait());
    }

    @Test(priority = 1, description = "Adding a new holiday with valid data should succeed")
    public void testAddHoliday() throws InterruptedException {
        System.out.println("\n---------- HOLIDAY MASTER: Add Holiday ----------");
        holidayPage.open(baseUrl);

        holidayPage.clickAddHoliday();
        holidayPage.fillHolidayForm("TestHol_" + System.currentTimeMillis());
        holidayPage.submitForm();

        Assert.assertTrue(
            holidayPage.isAddSuccessful(),
            "Success message should appear after adding a holiday"
        );
        System.out.println("PASS: Holiday Added");
    }

    @Test(priority = 2, description = "Adding a holiday that already exists should show a duplicate error")
    public void testDuplicateHolidayValidation() throws InterruptedException {
        System.out.println("\n---------- HOLIDAY MASTER: Duplicate Validation ----------");
        String dupName = "DupHol_" + System.currentTimeMillis();

        // First insert: should succeed and guarantees the name now exists.
        holidayPage.open(baseUrl);
        holidayPage.clickAddHoliday();
        holidayPage.fillHolidayForm(dupName);
        holidayPage.submitForm();

        // Second insert with the same name + same company + same date: should trigger duplicate validation.
        holidayPage.open(baseUrl);
        holidayPage.clickAddHoliday();
        holidayPage.fillHolidayForm(dupName);
        holidayPage.submitForm();

        Assert.assertTrue(
            holidayPage.isDuplicateErrorShown(),
            "Duplicate-Holiday error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last holiday in the list should complete without error")
    public void testDeleteHoliday() throws InterruptedException {
        System.out.println("\n---------- HOLIDAY MASTER: Delete Holiday ----------");
        holidayPage.open(baseUrl);

        holidayPage.deleteLastHoliday();

        System.out.println("PASS: Delete Action Performed");
    }
}
