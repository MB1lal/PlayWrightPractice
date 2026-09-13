package io.github.mb1lal.playwright.ui.pages.herokuapp;

import io.github.mb1lal.playwright.base.TestContext;
import io.github.mb1lal.playwright.ui.pages.BasePage;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Multiple Windows page ({@code /windows}): the link opens a new tab.
 */
@Slf4j
public class MultipleWindowsPage extends BasePage {

    public MultipleWindowsPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /** Click "Click Here" and return the newly opened page. */
    public Page openNewWindow() {
        Page popup = page.waitForPopup(() ->
                click(page.locator("text=Click Here").first(), "Click Here link"));
        popup.waitForLoadState();
        log.info("New window opened at {}", popup.url());
        return popup;
    }
}
