import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports multiple_window_tests.cy.js (no target-stripping workaround needed). */
test.describe('Multiple windows', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Multiple Windows');
    await expect(page).toHaveURL(/\/windows/);
  });

  test('new tab shows the expected content', async ({ windowsPage, page }) => {
    const popup = await windowsPage.openNewWindow();

    expect(popup.url()).toContain('/windows/new');
    expect(await popup.locator('h3').innerText()).toBe('New Window');
    await popup.close();

    expect(await page.locator('text=Click Here').first().isVisible()).toBe(true);
  });
});
