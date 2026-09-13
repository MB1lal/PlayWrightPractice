package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.MouseButton;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Context Menu page ({@code /context_menu}): right-clicking the hot-spot
 * raises a JS alert.
 */
@Slf4j
public class ContextMenuPage extends BasePage {

    public ContextMenuPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /** Right-click the hot-spot and return the resulting alert text. */
    public String rightClickHotSpot() {
        AtomicReference<String> alertText = new AtomicReference<>();
        page.onceDialog(dialog -> {
            alertText.set(dialog.message());
            dialog.dismiss();
        });
        page.locator("#hot-spot").click(
                new Locator.ClickOptions().setButton(MouseButton.RIGHT));
        log.info("Context-click alert text: '{}'", alertText.get());
        return alertText.get();
    }
}
