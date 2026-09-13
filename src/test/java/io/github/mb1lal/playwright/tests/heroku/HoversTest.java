package io.github.mb1lal.playwright.tests.herokuapp;

import io.github.mb1lal.playwright.support.BaseUiTest;
import io.github.mb1lal.playwright.ui.pages.herokuapp.HerokuHomePage;
import io.github.mb1lal.playwright.ui.pages.herokuapp.HoversPage;
import io.github.mb1lal.playwright.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code hover_tests.cy.js}: hovering each avatar reveals its profile name.
 */
@Tag("ui")
@DisplayName("Hovers")
class HoversTest extends BaseUiTest {

    private HoversPage hovers;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Hovers");
        AssertionHelper.assertTrue(browser.url().contains("/hovers"),
                "Should be on hovers but was " + browser.url());
        hovers = new HoversPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Each avatar reveals its profile name on hover")
    void profileNamesRevealed() {
        assertThat(hovers.hoverFigure(0)).contains("name: user1");
        assertThat(hovers.hoverFigure(1)).contains("name: user2");
        assertThat(hovers.hoverFigure(2)).contains("name: user3");
    }
}
