package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.DynamicLoadingPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code dynamic_loading_tests.cy.js}.
 */
@Tag("ui")
@DisplayName("Dynamic loading")
class DynamicLoadingTest extends BaseUiTest {

    private DynamicLoadingPage loadingPage;

    @BeforeEach
    void navigateToDynamicLoading() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Dynamic Loading");
        AssertionHelper.assertTrue(browser.url().contains("/dynamic_loading"),
                "Should be on dynamic loading but was " + browser.url());
        loadingPage = new DynamicLoadingPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Hidden element renders after Start (example 1)")
    void hiddenElementLoads() {
        loadingPage.openExample(1);
        AssertionHelper.assertTrue(browser.url().contains("dynamic_loading/1"),
                "Should be on example 1 but was " + browser.url());

        loadingPage.start();
        assertThat(loadingPage.finishText()).isEqualTo("Hello World!");
    }

    @Test
    @DisplayName("Newly created element renders after Start (example 2)")
    void renderedElementLoads() {
        loadingPage.openExample(2);
        AssertionHelper.assertTrue(browser.url().contains("dynamic_loading/2"),
                "Should be on example 2 but was " + browser.url());

        loadingPage.start();
        assertThat(loadingPage.finishText()).isEqualTo("Hello World!");
    }
}
