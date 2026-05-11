package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.LocationMasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Location Master module.
 */
public class LocationMasterTest extends BaseTest {

    private LocationMasterPage locationPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }

        locationPage = new LocationMasterPage(driver, ConfigReader.getExplicitWait());
    }

    @Test(priority = 1, description = "Adding a new location with valid data should succeed")
    public void testAddLocation() throws InterruptedException {
        System.out.println("\n---------- LOCATION MASTER: Add Location ----------");
        locationPage.open(baseUrl);

        locationPage.clickAddLocation();
        locationPage.fillLocationForm(
            "TestLoc_" + System.currentTimeMillis(),
            "Maharashtra",
            "1 Test Street",
            "400001",
            "Mumbai"
        );
        locationPage.submitForm();

        Assert.assertTrue(
            locationPage.isAddSuccessful(),
            "Success message should appear after adding a location"
        );
        System.out.println("PASS: Location Added");
    }

    @Test(priority = 2, description = "Adding a location that already exists should show a duplicate error")
    public void testDuplicateLocationValidation() throws InterruptedException {
        System.out.println("\n---------- LOCATION MASTER: Duplicate Validation ----------");
        String dupName = "DupLoc_" + System.currentTimeMillis();

        // First insert: should succeed and guarantees the location now exists.
        locationPage.open(baseUrl);
        locationPage.clickAddLocation();
        locationPage.fillLocationForm(dupName, "Maharashtra", "1 Test Street", "400001", "Mumbai");
        locationPage.submitForm();

        // Second insert with the same location + company: should trigger duplicate validation.
        locationPage.open(baseUrl);
        locationPage.clickAddLocation();
        locationPage.fillLocationForm(dupName, "Maharashtra", "1 Test Street", "400001", "Mumbai");
        locationPage.submitForm();

        Assert.assertTrue(
            locationPage.isDuplicateErrorShown(),
            "Duplicate-Location error should appear"
        );
        System.out.println("PASS: Duplicate Validation");
    }

    @Test(priority = 3, description = "Deleting the last location in the list should complete without error")
    public void testDeleteLocation() throws InterruptedException {
        System.out.println("\n---------- LOCATION MASTER: Delete Location ----------");
        locationPage.open(baseUrl);

        locationPage.deleteLastLocation();

        System.out.println("PASS: Delete Action Performed");
    }
}
