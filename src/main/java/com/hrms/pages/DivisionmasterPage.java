// package com.hrms.pages;

// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.support.ui.Select;

// import java.util.List;

// /**
//  * Page Object for Department Master.
//  */
// public class DivisionmasterPage extends BasePage {

//     public DivisionmasterPage(WebDriver driver, int waitSeconds) {
//         super(driver, waitSeconds);
//     }

//     // ── Locators ─────────────────────────────────────────────
//     private final By addDivisionBtn =
//             By.xpath("//button[contains(., 'Add Division')]");

//     private final By divisionNameField =
//             By.cssSelector("input[formcontrolname='departmentName']");

//     private final By departmentDescriptionField =
//             By.cssSelector("input[formcontrolname='departmentDescription']");

//     private final By companyDropdown = By.id("companyId");

//     private final By submitBtn =
//             By.xpath("//button[normalize-space()='Submit']");

//     private final By tableRows = By.cssSelector("table tbody tr");

//     private final By lastRowDeleteBtn =
//             By.cssSelector("table tbody tr:last-child button, table tbody tr:last-child [class*='delete'], table tbody tr:last-child a");

//     private final By confirmYesBtn =
//             By.xpath("//button[contains(text(),'Yes') or contains(text(),'OK') or contains(text(),'Confirm')]");

//     // ── Navigation ───────────────────────────────────────────
//     public void open(String baseUrl) {
//         driver.get(baseUrl + "/#/master-management/department-master");
//     }

//     // ── Actions ──────────────────────────────────────────────
//     public void clickAddDepartment() {
//         waitClickable(addDepartmentBtn).click();
//         pause(1500); // modal animation
//     }

//     public void fillDepartmentForm(String name, String description) {
//         WebElement nameEl = waitVisible(departmentNameField);
//         nameEl.clear();
//         nameEl.sendKeys(name);

//         if (description != null) {
//             WebElement descEl = waitVisible(departmentDescriptionField);
//             descEl.clear();
//             descEl.sendKeys(description);
//         }

//         selectFirstCompany();
//     }

//     public void selectCompany(String companyName) {
//         Select select = new Select(waitVisible(companyDropdown));
//         select.selectByVisibleText(companyName);
//     }

//     /** Picks the first non-placeholder option in the company dropdown. */
//     private void selectFirstCompany() {
//         Select select = new Select(waitVisible(companyDropdown));
//         List<WebElement> options = select.getOptions();
//         for (WebElement option : options) {
//             String disabled = option.getAttribute("disabled");
//             String value = option.getAttribute("value");
//             if (disabled == null && value != null && !value.equals("null") && !value.isEmpty()) {
//                 select.selectByValue(value);
//                 return;
//             }
//         }
//     }

//     public void submitForm() {
//         jsClick(submitBtn);
//         pause(2000);
//     }

//     // ── Verification ─────────────────────────────────────────
//     public boolean isAddSuccessful() {
//         return isTextPresent("success") || isTextPresent("added");
//     }

//     public boolean isDuplicateErrorShown() {
//         pause(1500);
//         return isTextPresent("exists")
//             || isTextPresent("duplicate")
//             || isTextPresent("already")
//             || isTextPresent("same name")
//             || isTextPresent("taken");
//     }

//     // ── Delete ───────────────────────────────────────────────
//     public void deleteLastDepartment() {
//         org.openqa.selenium.support.ui.WebDriverWait shortWait =
//                 new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
//         try {
//             shortWait.until(d -> !d.findElements(tableRows).isEmpty());
//         } catch (Exception ignored) {
//             return; // no data
//         }
//         try {
//             waitClickable(lastRowDeleteBtn).click();
//             try {
//                 waitClickable(confirmYesBtn).click();
//             } catch (Exception ignored) {}
//         } catch (Exception ignored) {}
//     }
// }
