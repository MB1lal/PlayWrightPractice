import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports context_menu_tests.cy.js. */
test.describe('Context menu', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Context Menu');
    await expect(page).toHaveURL(/\/context_menu/);
  });

  test('right-clicking the hot-spot raises the expected alert', async ({ contextMenuPage }) => {
    expect(await contextMenuPage.rightClickHotSpot()).toBe('You selected a context menu');
  });
});
