package com.hrms.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Letter Template Master.
 *
 * Notable quirks:
 *  - The two template dropdowns are PrimeNG <p-dropdown> widgets (not native &lt;select&gt;),
 *    so we click to open and then click the matching option text — no Selenium Select.
 *  - The body is a Quill rich-text editor (contenteditable div, .ql-editor).
 *  - The submit button uses PrimeNG markup: a &lt;button&gt; whose label is a child &lt;span&gt;.
 */
public class LettermanagementMasterPage extends BasePage {

    public LettermanagementMasterPage(WebDriver driver, int waitSeconds) {
        super(driver, waitSeconds);
    }

    // ── Locators ─────────────────────────────────────────────
    private final By breadcrumb =
            By.xpath("//*[normalize-space()='Letter Template Master']");

    // First p-dropdown = template type (Offer Letter, Appointment Letter, ...)
    private final By templateDropdown = By.xpath("(//p-dropdown)[1]");
    // Second p-dropdown = field variable to insert (candidateName, ...)
    private final By fieldDropdown    = By.xpath("(//p-dropdown)[2]");

    // The visible text label inside each dropdown
    private final By templateLabel =
            By.xpath("(//p-dropdown)[1]//span[contains(@class,'p-dropdown-label')]");
    private final By fieldLabel =
            By.xpath("(//p-dropdown)[2]//span[contains(@class,'p-dropdown-label')]");

    // Quill editor body — the contenteditable div
    private final By editorArea = By.cssSelector(".ql-editor");

    // Quill toolbar buttons (aria-label is stable)
    private final By boldBtn      = By.cssSelector("button.ql-bold");
    private final By italicBtn    = By.cssSelector("button.ql-italic");
    private final By underlineBtn = By.cssSelector("button.ql-underline");

    // PrimeNG submit — span carries the text inside a wrapping button
    private final By submitBtn =
            By.xpath("//button[.//span[normalize-space()='Submit']]");

    // ── Navigation ───────────────────────────────────────────
    public void open(String baseUrl) {
        driver.get(baseUrl + "/#/master-management/letter-template-master");
        pause(1500);
        try {
            wait.until(d -> !d.findElements(templateDropdown).isEmpty());
        } catch (Exception ignored) {}
    }

    // ── Actions ──────────────────────────────────────────────
    public void selectTemplate(String optionText) {
        clickDropdownOption(templateDropdown, optionText);
    }

    public void selectField(String optionText) {
        clickDropdownOption(fieldDropdown, optionText);
    }

    /** Click the option whose visible text matches {@code optionText}. */
    private void clickDropdownOption(By dropdown, String optionText) {
        WebElement dd = waitClickable(dropdown);
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", dd);
        dd.click();
        pause(500);

        By optionByText = By.xpath(
            "//li[contains(@class,'p-dropdown-item') and normalize-space()='" + optionText + "']");
        try {
            waitClickable(optionByText).click();
        } catch (Exception e) {
            // Fallback: first non-empty option
            By anyOption = By.cssSelector(".p-dropdown-panel .p-dropdown-item");
            try { waitClickable(anyOption).click(); } catch (Exception ignored) {}
        }
        pause(500);
    }

    public void typeInEditor(String text) {
        WebElement editor = waitVisible(editorArea);
        editor.click();
        editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        editor.sendKeys(Keys.DELETE);
        editor.sendKeys(text);
    }

    public void clearEditor() {
        WebElement editor = waitVisible(editorArea);
        editor.click();
        editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        editor.sendKeys(Keys.DELETE);
    }

    public void clickBold()      { try { waitClickable(boldBtn).click(); } catch (Exception ignored) {} }
    public void clickItalic()    { try { waitClickable(italicBtn).click(); } catch (Exception ignored) {} }
    public void clickUnderline() { try { waitClickable(underlineBtn).click(); } catch (Exception ignored) {} }

    public void submitForm() {
        jsClick(submitBtn);
        pause(2000);
    }

    // ── Verifications ────────────────────────────────────────

    /** True if the breadcrumb / URL confirm we're on Letter Template Master. */
    public boolean isOnPage() {
        boolean urlMatch  = driver.getCurrentUrl().contains("letter-template-master");
        boolean crumbMatch = !driver.findElements(breadcrumb).isEmpty();
        return urlMatch && crumbMatch;
    }

    /** Browser tab title. */
    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getSelectedTemplate() {
        try { return waitVisible(templateLabel).getText().trim(); }
        catch (Exception e) { return ""; }
    }

    public String getSelectedField() {
        try { return waitVisible(fieldLabel).getText().trim(); }
        catch (Exception e) { return ""; }
    }

    public String getEditorContent() {
        try { return waitVisible(editorArea).getText().trim(); }
        catch (Exception e) { return ""; }
    }

    public boolean isEditorEmpty() {
        return getEditorContent().isEmpty();
    }

    /** True if any required-field validation is visible after a submit attempt. */
    public boolean isMandatoryValidationShown() {
        pause(1000);
        return isTextPresent("required")
            || isTextPresent("please select")
            || isTextPresent("please enter")
            || isTextPresent("cannot be empty")
            || hasDuplicateOrErrorIndicator();
    }

    public boolean isSubmitSuccessful() {
        return isTextPresent("success")
            || isTextPresent("saved")
            || isTextPresent("submitted")
            || isTextPresent("updated");
    }
}
