package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.CheckboxesPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Ports {@code checkboxes_tests.cy.js}.
 */
@Tag("ui")
@DisplayName("Checkboxes")
class CheckboxesTest extends BaseUiTest {

    private CheckboxesPage checkboxes;

    @BeforeEach
    void navigateToCheckboxes() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Checkboxes");
        AssertionHelper.assertTrue(browser.url().contains("/checkboxes"),
                "Should be on the checkboxes page but was " + browser.url());
        checkboxes = new CheckboxesPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Checkboxes can be selected and deselected")
    void selectionAndDeselection() {
        checkboxes.toggle(0);
        checkboxes.assertChecked(0, true);

        checkboxes.toggle(1);
        checkboxes.assertChecked(1, false);
    }
}
