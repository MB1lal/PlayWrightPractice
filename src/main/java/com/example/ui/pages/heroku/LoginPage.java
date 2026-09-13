package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Form Authentication page ({@code /login}) and the secure area it guards.
 */
@Slf4j
public class LoginPage extends BasePage {

    private final Locator username;
    private final Locator password;
    private final Locator loginButton;
    private final Locator flash;

    public LoginPage(Page page, TestContext testContext) {
        super(page, testContext);
        this.username = page.locator("#username");
        this.password = page.locator("#password");
        this.loginButton = page.locator("button.radius");
        this.flash = page.locator("#flash");
    }

    public void login(String user, String pass) {
        log.info("Logging in as '{}'", user);
        fill(username, user, "username field");
        fill(password, pass, "password field");
        click(loginButton, "login button");
        page.waitForLoadState();
    }

    public void logout() {
        click(page.locator("a[href='/logout']"), "logout button");
        page.waitForLoadState();
    }

    /** Flash banners include a close icon, so match on containment. */
    public void assertFlashContains(String expected) {
        assertContainsText(flash, expected, "flash message");
    }
}
