package io.github.mb1lal.playwright.ui.pages.herokuapp;

import io.github.mb1lal.playwright.base.TestContext;
import io.github.mb1lal.playwright.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Hovers page ({@code /hovers}): hovering an avatar reveals its profile caption.
 */
@Slf4j
public class HoversPage extends BasePage {

    public HoversPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /** Hover figure {@code index} (zero-based) and return its revealed profile name. */
    public String hoverFigure(int index) {
        Locator figure = page.locator(".figure").nth(index);
        figure.hover();
        Locator caption = figure.locator(".figcaption h5").first();
        assertIsVisible(caption, "profile caption " + index);
        String name = getText(caption, "profile caption " + index);
        log.info("Figure {} reveals '{}'", index, name);
        return name;
    }
}
