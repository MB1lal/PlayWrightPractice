package io.github.mb1lal.playwright.ui.pages.herokuapp;

import io.github.mb1lal.playwright.base.TestContext;
import io.github.mb1lal.playwright.ui.pages.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;

/**
 * Frames hub ({@code /frames}) with nested-frames and iframe-editor support.
 */
@Slf4j
public class FramesPage extends BasePage {

    public FramesPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    public void openNestedFrames() {
        click(page.getByRole(AriaRole.LINK,
                        new Page.GetByRoleOptions().setName("Nested Frames").setExact(true)),
                "Nested Frames link");
        page.waitForLoadState();
    }

    public void openIframeEditor() {
        click(page.getByRole(AriaRole.LINK,
                        new Page.GetByRoleOptions().setName("iFrame").setExact(true)),
                "iFrame link");
        page.waitForLoadState();
    }

    /** Body text of a nested frame: {@code left}, {@code middle}, {@code right} or {@code bottom}. */
    public String nestedFrameText(String position) {
        String text;
        if ("bottom".equalsIgnoreCase(position)) {
            text = page.frameLocator("frame[src='/frame_bottom']").locator("body").innerText();
        } else {
            text = page.frameLocator("frame[src='/frame_top']")
                    .frameLocator("frame[src='/frame_" + position.toLowerCase() + "']")
                    .locator("body").innerText();
        }
        log.debug("Nested frame '{}' text: '{}'", position, text.trim());
        return text.trim();
    }

    /**
     * Assert the TinyMCE iframe is present and visible.
     *
     * <p>NOTE: the demo site's TinyMCE cloud key is out of editor loads, so the
     * editor renders read-only with no content — reading or writing its body is
     * impossible. Presence of the iframe is what's asserted; this still proves
     * iframe handling, which is the point of the spec.
     */
    public void assertEditorPresent() {
        assertIsVisible(page.locator("#mce_0_ifr"), "TinyMCE editor iframe");
    }
}
