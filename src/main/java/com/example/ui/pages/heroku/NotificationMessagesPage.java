package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * Notification Messages page ({@code /notification_message}).
 * The flash banner shows one of two messages at random, exactly as the
 * Cypress spec models it.
 */
@Slf4j
public class NotificationMessagesPage extends BasePage {

    private static final Set<String> VALID_MESSAGES = Set.of(
            "Action successful",
            "Action unsuccesful, please try again");

    public NotificationMessagesPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /** Current flash text with the close icon stripped (mirrors the Cypress assertion). */
    public String flashText() {
        Locator flash = page.locator("#flash");
        flash.waitFor();
        String text = flash.innerText().replace("×", "").trim();
        log.debug("Flash message: '{}'", text);
        return text;
    }

    public void assertFlashIsKnownMessage() {
        String text = flashText();
        com.example.utils.AssertionHelper.assertTrue(VALID_MESSAGES.contains(text),
                "Flash '" + text + "' should be one of " + VALID_MESSAGES);
    }

    public void loadNewMessage() {
        click(page.locator("p > a"), "load new message link");
    }
}
