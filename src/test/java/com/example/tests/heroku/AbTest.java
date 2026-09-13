package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.AbTestPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code ab_testing_tests.cy.js}.
 *
 * <p>Note: the live site's bucketing has changed since the Cypress specs were
 * written — with the opt-out cookie the page now deterministically renders
 * {@code "No A/B Test"}. Expectations below match current server behavior
 * (verified), not the stale Cypress comments.
 */
@Tag("ui")
@DisplayName("A/B testing")
class AbTest extends BaseUiTest {

    @Test
    @DisplayName("Opt-out cookie renders the control heading, stable across reload")
    void optOutCookieShowsControl() {
        browser.addCookie("optimizelyOptOut", "true", "the-internet.herokuapp.com");

        AbTestPage abTest = new AbTestPage(browser.getPage(), context);
        abTest.open(config.getHerokuUrl());
        assertThat(abTest.heading()).isEqualTo("No A/B Test");

        browser.getPage().reload();
        assertThat(abTest.heading()).isEqualTo("No A/B Test");
    }

    @Test
    @DisplayName("Opt-out URL raises the success alert and shows the control heading")
    void optOutUrlShowsAlert() {
        AbTestPage abTest = new AbTestPage(browser.getPage(), context);

        String alertText = abTest.openOptOutUrl(config.getHerokuUrl());
        assertThat(alertText).isEqualTo(AbTestPage.OPT_OUT_MESSAGE);
        assertThat(abTest.heading()).isEqualTo("No A/B Test");
    }
}
