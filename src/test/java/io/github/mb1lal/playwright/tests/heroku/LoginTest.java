package io.github.mb1lal.playwright.tests.herokuapp;

import io.github.mb1lal.playwright.support.BaseUiTest;
import io.github.mb1lal.playwright.ui.pages.herokuapp.HerokuHomePage;
import io.github.mb1lal.playwright.ui.pages.herokuapp.LoginPage;
import io.github.mb1lal.playwright.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Ports {@code authentication_tests.cy.js}: form login, logout and invalid credentials.
 */
@Tag("ui")
@DisplayName("Form authentication")
class LoginTest extends BaseUiTest {

    private LoginPage loginPage;

    @BeforeEach
    void navigateToLogin() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Form Authentication");
        AssertionHelper.assertTrue(browser.url().contains("/login"),
                "Should be on the login page but was " + browser.url());
        loginPage = new LoginPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Valid credentials log into the secure area")
    void loginWithValidCredentials() {
        loginPage.login("tomsmith", "SuperSecretPassword!");
        loginPage.assertFlashContains("You logged into a secure area!");
    }

    @Test
    @DisplayName("Login followed by logout shows the logout flash")
    void logoutJourney() {
        loginPage.login("tomsmith", "SuperSecretPassword!");
        loginPage.assertFlashContains("You logged into a secure area!");

        loginPage.logout();
        loginPage.assertFlashContains("You logged out of the secure area!");
    }

    @Test
    @DisplayName("Invalid username shows an error flash")
    void loginWithInvalidCredentials() {
        loginPage.login("test", "SuperSecretPassword!");
        loginPage.assertFlashContains("Your username is invalid!");
    }
}
