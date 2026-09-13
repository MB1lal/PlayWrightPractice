package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.BrokenImagesPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code broken_images_tests.cy.js}.
 */
@Tag("ui")
@DisplayName("Broken images")
class BrokenImagesTest extends BaseUiTest {

    private BrokenImagesPage imagesPage;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Broken Images");
        AssertionHelper.assertTrue(browser.url().contains("/broken_images"),
                "Should be on broken images but was " + browser.url());
        imagesPage = new BrokenImagesPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Known-broken images render at zero size")
    void brokenImagesHaveZeroSize() {
        assertThat(imagesPage.naturalWidth("asdf.jpg")).isZero();
        assertThat(imagesPage.naturalWidth("hjkl.jpg")).isZero();
    }

    @Test
    @DisplayName("Valid image renders with non-zero size")
    void validImageHasNonZeroSize() {
        assertThat(imagesPage.naturalWidth("img/avatar-blank.jpg")).isPositive();
    }
}
