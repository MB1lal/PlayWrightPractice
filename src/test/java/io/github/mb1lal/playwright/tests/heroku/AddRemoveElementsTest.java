package io.github.mb1lal.playwright.tests.herokuapp;

import io.github.mb1lal.playwright.support.BaseUiTest;
import io.github.mb1lal.playwright.ui.pages.herokuapp.AddRemoveElementsPage;
import io.github.mb1lal.playwright.ui.pages.herokuapp.HerokuHomePage;
import io.github.mb1lal.playwright.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Ports {@code add_remove_elements_tests.cy.js}.
 */
@Tag("ui")
@DisplayName("Add/remove elements")
class AddRemoveElementsTest extends BaseUiTest {

    private AddRemoveElementsPage elementsPage;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Add/Remove Elements");
        AssertionHelper.assertTrue(browser.url().contains("/add_remove_elements"),
                "Should be on add/remove elements but was " + browser.url());
        elementsPage = new AddRemoveElementsPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Added element appears on the page")
    void addedElementAppears() {
        elementsPage.addElements(1);
        elementsPage.assertDeleteVisible(true);
    }

    @Test
    @DisplayName("Deleted element disappears from the page")
    void deletedElementDisappears() {
        elementsPage.addElements(1);
        elementsPage.assertDeleteVisible(true);

        elementsPage.deleteFirst();
        elementsPage.assertDeleteVisible(false);
    }
}
