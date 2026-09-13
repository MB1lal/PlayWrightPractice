package com.example.tests.herokuapp;

import com.example.playwright.BrowserManager;
import com.example.support.BaseUiTest;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Ports {@code basic_auth_tests.cy.js}: HTTP basic authentication.
 *
 * <p>Valid credentials are installed into the browser context before navigation
 * (the equivalent of Cypress's {@code auth} visit option); wrong credentials
 * render the server's 401 page.
 */
@Tag("ui")
@DisplayName("Basic auth")
class BasicAuthTest extends BaseUiTest {

    private static final String PAGE = "/basic_auth";

    @Test
    @DisplayName("Valid credentials show the congratulations page")
    void loginWithValidCredentials() {
        restartBrowserWithCredentials("admin", "admin");
        browser.navigate(config.getHerokuUrl() + PAGE);

        AssertionHelper.assertContains(browser.getPage().locator("body").innerText(),
                "Congratulations! You must have the proper credentials.", "basic auth page");
    }

    @Test
    @DisplayName("Invalid credentials show the not-authorized page")
    void loginWithInvalidCredentials() {
        restartBrowserWithCredentials("test", "test");
        browser.navigate(config.getHerokuUrl() + PAGE);

        String body = browser.getPage().locator("body").innerText();
        AssertionHelper.assertContains(body, "Not authorized", "basic auth rejection");
    }

    private void restartBrowserWithCredentials(String user, String password) {
        browser.close();
        browser = new BrowserManager(context);
        browser.startWithHttpCredentials(user, password);
    }
}
