package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Login screen.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeClass
    public void initPage() {
        loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
    }

    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Valid credentials should redirect to the dashboard")
    public void testValidLogin() throws InterruptedException {
        System.out.println("\n---------- LOGIN ----------");
        networkUtils.navigateSafely(baseUrl + "/#/auth/login");

        loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

        Assert.assertTrue(
            loginPage.isDashboardLoaded(),
            "Expected dashboard URL after successful login"
        );
        System.out.println("PASS: Login Successful");
    }
}
