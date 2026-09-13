package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.ui.pages.herokuapp.MultipleWindowsPage;
import com.example.utils.AssertionHelper;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code multiple_window_tests.cy.js}: the link opens a new tab whose
 * content is verified (no need for the Cypress {@code target} workaround —
 * Playwright captures the popup directly).
 */
@Tag("ui")
@DisplayName("Multiple windows")
class MultipleWindowsTest extends BaseUiTest {

    private MultipleWindowsPage windows;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Multiple Windows");
        AssertionHelper.assertTrue(browser.url().contains("/windows"),
                "Should be on windows but was " + browser.url());
        windows = new MultipleWindowsPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("New tab shows the expected content")
    void newWindowContent() {
        Page popup = windows.openNewWindow();

        assertThat(popup.url()).contains("/windows/new");
        assertThat(popup.locator("h3").innerText()).isEqualTo("New Window");

        popup.close();
        AssertionHelper.assertTrue(
                browser.getPage().locator("text=Click Here").first().isVisible(),
                "Original page should still show the link");
    }
}
