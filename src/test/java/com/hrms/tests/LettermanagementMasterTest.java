package com.hrms.tests;

import com.hrms.base.BaseTest;
import com.hrms.config.ConfigReader;
import com.hrms.pages.LettermanagementMasterPage;
import com.hrms.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests covering the Letter Template Master module.
 */
public class LettermanagementMasterTest extends BaseTest {

    private LettermanagementMasterPage letterPage;

    @BeforeClass
    public void loginAndInitPage() throws InterruptedException {
        if (driver.getCurrentUrl().contains("auth/login")) {
            LoginPage loginPage = new LoginPage(driver, ConfigReader.getExplicitWait());
            networkUtils.navigateSafely(baseUrl + "/#/auth/login");
            loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());
            loginPage.isDashboardLoaded();
        }
        letterPage = new LettermanagementMasterPage(driver, ConfigReader.getExplicitWait());
    }

    @Test(priority = 1, description = "Verify the page title / breadcrumb contains 'Letter Template Master'")
    public void testPageTitle() {
        System.out.println("\n---------- LETTER TEMPLATE MASTER: Page Title ----------");
        letterPage.open(baseUrl);

        Assert.assertTrue(
            letterPage.isOnPage(),
            "Breadcrumb should contain 'Letter Template Master' and URL should match"
        );
        System.out.println("Browser title: " + letterPage.getPageTitle());
        System.out.println("PASS: Page title / breadcrumb verified");
    }

    @Test(priority = 2, description = "Verify the template dropdown selects an option correctly")
    public void testSelectTemplate() {
        System.out.println("\n---------- LETTER TEMPLATE MASTER: Select Template ----------");
        letterPage.open(baseUrl);
        letterPage.selectTemplate("Offer Letter");

        String selected = letterPage.getSelectedTemplate();
        System.out.println("Selected template: '" + selected + "'");
        Assert.assertEquals(selected, "Offer Letter",
            "Template dropdown should show 'Offer Letter' after selection");
        System.out.println("PASS: Template selected");
    }

    @Test(priority = 3, description = "Verify the field-variable dropdown selects an option correctly")
    public void testSelectFieldVariable() {
        System.out.println("\n---------- LETTER TEMPLATE MASTER: Select Field Variable ----------");
        letterPage.open(baseUrl);
        letterPage.selectTemplate("Offer Letter");
        letterPage.selectField("candidateName");

        String selected = letterPage.getSelectedField();
        System.out.println("Selected field: '" + selected + "'");
        Assert.assertEquals(selected, "candidateName",
            "Field dropdown should show 'candidateName' after selection");
        System.out.println("PASS: Field variable selected");
    }

    @Test(priority = 4, description = "Verify the rich-text editor accepts and retains typed input")
    public void testEditorAcceptsInput() throws InterruptedException {
        System.out.println("\n---------- LETTER TEMPLATE MASTER: Editor Input ----------");
        letterPage.open(baseUrl);
        letterPage.selectTemplate("Offer Letter");

        String body = "Dear candidate, welcome to the company. " + System.currentTimeMillis();
        letterPage.typeInEditor(body);

        String content = letterPage.getEditorContent();
        System.out.println("Editor content: '" + content + "'");
        Assert.assertTrue(content.contains("welcome to the company"),
            "Editor should retain the typed body text");
        System.out.println("PASS: Editor accepts input");
    }

    @Test(priority = 5, description = "Verify mandatory-field validation when submitting with empty editor")
    public void testMandatoryValidation() throws InterruptedException {
        System.out.println("\n---------- LETTER TEMPLATE MASTER: Mandatory Validation ----------");
        letterPage.open(baseUrl);
        // Do NOT select template, do NOT type body — try a direct submit.
        letterPage.submitForm();

        Assert.assertTrue(
            letterPage.isMandatoryValidationShown(),
            "Required-field validation should be shown when the form is submitted empty"
        );
        System.out.println("PASS: Mandatory validation surfaced");
    }

    @Test(priority = 6, description = "Verify submit success when all required fields are filled")
    public void testSubmitWithValidData() throws InterruptedException {
        System.out.println("\n---------- LETTER TEMPLATE MASTER: Submit ----------");
        letterPage.open(baseUrl);

        letterPage.selectTemplate("Offer Letter");
        letterPage.selectField("candidateName");
        letterPage.typeInEditor(
            "Dear {candidateName}, congratulations on your offer. " + System.currentTimeMillis()
        );
        letterPage.submitForm();

        Assert.assertTrue(
            letterPage.isSubmitSuccessful(),
            "A success message should appear after submitting a valid letter template"
        );
        System.out.println("PASS: Letter template submitted");
    }
}
