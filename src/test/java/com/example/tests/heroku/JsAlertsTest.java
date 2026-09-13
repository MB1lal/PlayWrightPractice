package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.ui.pages.herokuapp.JsAlertsPage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code js_alerts_tests.cy.js}: alert, confirm and prompt dialogs.
 */
@Tag("ui")
@DisplayName("JavaScript alerts")
class JsAlertsTest extends BaseUiTest {

    private JsAlertsPage alerts;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("JavaScript Alerts");
        AssertionHelper.assertTrue(browser.url().contains("/javascript_alerts"),
                "Should be on javascript alerts but was " + browser.url());
        alerts = new JsAlertsPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("JS alert text matches and success message appears")
    void jsAlert() {
        String message = alerts.clickAndHandleDialog("Click for JS Alert",
                com.microsoft.playwright.Dialog::accept);

        assertThat(message).isEqualTo("I am a JS Alert");
        alerts.assertResultContains("You successfully clicked an alert");
    }

    @Test
    @DisplayName("Accepting the confirm shows the Ok result")
    void jsConfirm() {
        String message = alerts.clickAndHandleDialog("Click for JS Confirm",
                com.microsoft.playwright.Dialog::accept);

        assertThat(message).isEqualTo("I am a JS Confirm");
        alerts.assertResultContains("You clicked: Ok");
    }

    @Test
    @DisplayName("Answering the prompt echoes the entered text")
    void jsPrompt() {
        // NOTE: the live page says "I am a JS prompt" (lowercase p),
        // unlike the stale Cypress expectation.
        String message = alerts.clickAndHandleDialog("Click for JS Prompt",
                dialog -> dialog.accept("Test"));

        assertThat(message).isEqualTo("I am a JS prompt");
        alerts.assertResultContains("You entered: Test");
    }
}
