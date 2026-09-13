package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * JavaScript Alerts page ({@code /javascript_alerts}).
 */
@Slf4j
public class JsAlertsPage extends BasePage {

    public JsAlertsPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /** Click a button that raises a dialog; the handler runs before the click resolves. */
    public String clickAndHandleDialog(String buttonText, java.util.function.Consumer<com.microsoft.playwright.Dialog> handler) {
        AtomicReference<String> message = new AtomicReference<>();
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            handler.accept(dialog);
        });
        click(page.locator("text=" + buttonText).first(), buttonText);
        return message.get();
    }

    public String resultText() {
        return getText(page.locator("#result"), "result text");
    }

    public void assertResultContains(String expected) {
        assertContainsText(page.locator("#result"), expected, "result text");
    }
}
