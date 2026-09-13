package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;

/**
 * Landing page of the-internet.herokuapp.com.
 * Mirrors {@code navigateToXPage} from the Cypress support commands.
 */
@Slf4j
public class HerokuHomePage extends BasePage {

    public HerokuHomePage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    public void open(String baseUrl) {
        log.info("Opening Herokuapp home at {}", baseUrl);
        page.navigate(baseUrl);
        page.waitForLoadState();
    }

    /**
     * Follow the homepage link with the given accessible name.
     * Exact matching is required: role-name lookup is substring-based, so
     * "Frames" would otherwise also match "Nested Frames".
     */
    public void goTo(String linkName) {
        log.info("Navigating to '{}'", linkName);
        click(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName(linkName).setExact(true)), linkName);
        page.waitForLoadState();
    }
}
