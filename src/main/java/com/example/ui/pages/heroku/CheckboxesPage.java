package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.example.utils.AssertionHelper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Checkboxes page ({@code /checkboxes}).
 */
@Slf4j
public class CheckboxesPage extends BasePage {

    public CheckboxesPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    private Locator box(int index) {
        return page.locator("#checkboxes > input").nth(index);
    }

    public void toggle(int index) {
        click(box(index), "checkbox " + index);
    }

    public void assertChecked(int index, boolean expected) {
        boolean actual = box(index).isChecked();
        AssertionHelper.assertTrue(actual == expected,
                "Checkbox " + index + " checked should be " + expected);
    }
}
