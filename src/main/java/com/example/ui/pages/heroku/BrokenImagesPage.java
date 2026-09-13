package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Broken Images page ({@code /broken_images}).
 */
@Slf4j
public class BrokenImagesPage extends BasePage {

    public BrokenImagesPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /**
     * Returns the rendered width of an image; broken images render at 0px
     * (mirrors the Cypress {@code naturalWidth} checks).
     */
    public int naturalWidth(String src) {
        Locator image = page.locator("img[src='" + src + "']");
        image.waitFor();
        Object width = image.evaluate("el => el.naturalWidth");
        int result = ((Number) width).intValue();
        log.debug("Image '{}' naturalWidth={}", src, result);
        return result;
    }
}
