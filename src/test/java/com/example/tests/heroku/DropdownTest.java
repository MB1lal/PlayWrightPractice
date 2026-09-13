package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.DropdownPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Ports {@code dropdown_tests.cy.js}.
 */
@Tag("ui")
@DisplayName("Dropdown")
class DropdownTest extends BaseUiTest {

    private DropdownPage dropdown;

    @BeforeEach
    void navigateToDropdown() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Dropdown");
        AssertionHelper.assertTrue(browser.url().contains("/dropdown"),
                "Should be on the dropdown page but was " + browser.url());
        dropdown = new DropdownPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Option 2 can be selected")
    void selectOptionTwo() {
        dropdown.selectByLabel("Option 2");
        dropdown.assertSelected("Option 2");
    }
}
