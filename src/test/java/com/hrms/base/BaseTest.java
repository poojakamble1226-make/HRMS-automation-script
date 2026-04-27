package com.hrms.base;

import com.hrms.config.ConfigReader;
import com.hrms.driver.DriverManager;
import com.hrms.utils.NetworkUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * Root test class.
 *
 * Responsibilities:
 *  - Start / stop the browser for the entire suite.
 *  - Run internet + site reachability checks once before any test executes.
 *  - Expose driver, baseUrl and networkUtils to every subclass.
 */
public class BaseTest {

    protected static WebDriver    driver;
    protected static String       baseUrl;
    protected static NetworkUtils networkUtils;

    @BeforeSuite(alwaysRun = true)
    public void suiteSetUp() throws InterruptedException {
        DriverManager.initDriver(ConfigReader.getBrowser());
        driver       = DriverManager.getDriver();
        baseUrl      = ConfigReader.getBaseUrl();
        networkUtils = new NetworkUtils(driver, ConfigReader.getExplicitWait());

        System.out.println("\n========== PRE-FLIGHT CHECKS ==========");

        System.out.println("Checking internet connection...");
        if (!networkUtils.isInternetAvailable()) {
            throw new RuntimeException("No internet. Check your connection and retry.");
        }
        System.out.println("Internet OK");

        System.out.println("Checking if site is reachable: " + baseUrl);
        if (!networkUtils.isSiteReachable(baseUrl)) {
            throw new RuntimeException("Site unreachable: " + baseUrl
                    + " — check VPN or ask the admin.");
        }
        System.out.println("Site is reachable");
        System.out.println("========================================\n");
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTearDown() {
        DriverManager.quitDriver();
    }
}
