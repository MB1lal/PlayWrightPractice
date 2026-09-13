import { NotificationMessagesPage } from '../../src/pages/heroku/notification-messages.page';
import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports notifciation_tests.cy.js. */
test.describe('Notification messages', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Notification Messages');
    await expect(page).toHaveURL(/\/notification_message/);
  });

  test('flash shows a known message, then another after reload link', async ({
    notificationsPage,
  }) => {
    expect(NotificationMessagesPage.VALID_MESSAGES).toContain(await notificationsPage.flashText());
    await notificationsPage.loadNewMessage();
    expect(NotificationMessagesPage.VALID_MESSAGES).toContain(await notificationsPage.flashText());
  });
});
