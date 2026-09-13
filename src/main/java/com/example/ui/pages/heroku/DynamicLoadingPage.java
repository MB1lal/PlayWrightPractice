package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Dynamic Loading examples ({@code /dynamic_loading/1} and {@code /2}).
 */
@Slf4j
public class DynamicLoadingPage extends BasePage {

    public DynamicLoadingPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    public void openExample(int number) {
        Locator example = page.locator("a[href='/dynamic_loading/" + number + "']");
        click(example, "dynamic loading example " + number);
        page.waitForLoadState();
    }

    public void start() {
        click(page.locator("#start button"), "start button");
    }

    /** Waits for the async render to finish and returns its text. */
    public String finishText() {
        Locator finish = page.locator("#finish h4");
        finish.waitFor(new Locator.WaitForOptions().setState(
                com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        String text = getText(finish, "finish text");
        log.info("Dynamic loading finished with '{}'", text);
        return text;
    }
}
