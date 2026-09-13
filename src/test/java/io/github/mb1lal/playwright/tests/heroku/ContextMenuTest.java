package io.github.mb1lal.playwright.tests.herokuapp;

import io.github.mb1lal.playwright.support.BaseUiTest;
import io.github.mb1lal.playwright.ui.pages.herokuapp.ContextMenuPage;
import io.github.mb1lal.playwright.ui.pages.herokuapp.HerokuHomePage;
import io.github.mb1lal.playwright.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code context_menu_tests.cy.js}.
 */
@Tag("ui")
@DisplayName("Context menu")
class ContextMenuTest extends BaseUiTest {

    private ContextMenuPage contextMenu;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Context Menu");
        AssertionHelper.assertTrue(browser.url().contains("/context_menu"),
                "Should be on context menu but was " + browser.url());
        contextMenu = new ContextMenuPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Right-clicking the hot-spot raises the expected alert")
    void rightClickRaisesAlert() {
        assertThat(contextMenu.rightClickHotSpot()).isEqualTo("You selected a context menu");
    }
}
