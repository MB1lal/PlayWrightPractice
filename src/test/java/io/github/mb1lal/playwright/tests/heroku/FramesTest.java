package io.github.mb1lal.playwright.tests.herokuapp;

import io.github.mb1lal.playwright.support.BaseUiTest;
import io.github.mb1lal.playwright.ui.pages.herokuapp.FramesPage;
import io.github.mb1lal.playwright.ui.pages.herokuapp.HerokuHomePage;
import io.github.mb1lal.playwright.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code frames_tests.cy.js}: nested frames content and the iframe editor.
 */
@Tag("ui")
@DisplayName("Frames")
class FramesTest extends BaseUiTest {

    private FramesPage frames;

    @BeforeEach
    void navigateToFrames() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Frames");
        AssertionHelper.assertTrue(browser.url().contains("/frames"),
                "Should be on frames but was " + browser.url());
        frames = new FramesPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Nested frames expose the expected body text")
    void nestedFramesContent() {
        frames.openNestedFrames();
        AssertionHelper.assertTrue(browser.url().contains("/nested_frames"),
                "Should be on nested frames but was " + browser.url());

        assertThat(frames.nestedFrameText("left")).contains("LEFT");
        assertThat(frames.nestedFrameText("middle")).contains("MIDDLE");
        assertThat(frames.nestedFrameText("right")).contains("RIGHT");
        assertThat(frames.nestedFrameText("bottom")).contains("BOTTOM");
    }

    @Test
    @DisplayName("Iframe editor is present on the page")
    void iframeEditorPresent() {
        frames.openIframeEditor();
        AssertionHelper.assertTrue(browser.url().contains("/iframe"),
                "Should be on the iframe page but was " + browser.url());

        frames.assertEditorPresent();
    }
}
