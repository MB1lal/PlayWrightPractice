package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.ui.pages.herokuapp.NotificationMessagesPage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Ports {@code notifciation_tests.cy.js}: the flash banner always shows one of
 * the two known messages, before and after loading a new one.
 */
@Tag("ui")
@DisplayName("Notification messages")
class NotificationMessagesTest extends BaseUiTest {

    private NotificationMessagesPage notifications;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("Notification Messages");
        notifications = new NotificationMessagesPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Flash shows a known message, then another after reload link")
    void flashMessagesAreKnown() {
        AssertionHelper.assertTrue(browser.url().contains("/notification_message"),
                "Should be on notification messages but was " + browser.url());

        notifications.assertFlashIsKnownMessage();
        notifications.loadNewMessage();
        notifications.assertFlashIsKnownMessage();
    }
}
