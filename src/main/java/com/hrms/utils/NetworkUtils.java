package com.hrms.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Pre-flight checks: internet connectivity and site reachability.
 * Also provides a safe navigation method with automatic retry.
 */
public class NetworkUtils {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    public NetworkUtils(WebDriver driver, int waitSeconds) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
    }

    /** Opens google.com to confirm internet is live. */
    public boolean isInternetAvailable() {
        try {
            driver.get("https://www.google.com");
            return driver.getTitle().toLowerCase().contains("google");
        } catch (Exception e) {
            return false;
        }
    }

    /** Attempts to load the target URL up to 3 times. */
    public boolean isSiteReachable(String url) {
        int retries = 3;
        while (retries-- > 0) {
            try {
                driver.get(url);
                if (!driver.getPageSource().contains("ERR_NAME_NOT_RESOLVED")) {
                    return true;
                }
            } catch (WebDriverException e) {
                if (e.getMessage() != null && e.getMessage().contains("ERR_NAME_NOT_RESOLVED")) {
                    System.out.println("  Retry " + (2 - retries) + "/3 – site not reachable...");
                    sleep(3000);
                } else {
                    return true; // site responded with a different kind of error – it's up
                }
            }
        }
        return false;
    }

    /**
     * Navigates to a URL and waits for the body element to be visible.
     * Retries up to 3 times on DNS failures.
     */
    public void navigateSafely(String url) throws InterruptedException {
        int retries = 3;
        while (retries-- > 0) {
            try {
                driver.get(url);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
                return;
            } catch (WebDriverException e) {
                if (e.getMessage() != null
                        && e.getMessage().contains("ERR_NAME_NOT_RESOLVED")
                        && retries > 0) {
                    System.out.println("  Retrying navigation to: " + url);
                    Thread.sleep(3000);
                } else {
                    throw e;
                }
            }
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
