package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import lombok.extern.slf4j.Slf4j;

/**
 * Dropdown page ({@code /dropdown}).
 */
@Slf4j
public class DropdownPage extends BasePage {

    private final Locator dropdown;

    public DropdownPage(Page page, TestContext testContext) {
        super(page, testContext);
        this.dropdown = page.locator("#dropdown");
    }

    public void selectByLabel(String label) {
        log.info("Selecting dropdown option '{}'", label);
        dropdown.selectOption(new SelectOption().setLabel(label));
    }

    public void assertSelected(String expectedLabel) {
        // NOTE: read via JS — <option> elements are never "visible", so the
        // standard wait-for-visible assertion helper cannot be used here.
        Object selected = dropdown.evaluate("el => el.selectedOptions[0].text");
        com.example.utils.AssertionHelper.assertContains(
                String.valueOf(selected), expectedLabel, "selected option");
    }
}
