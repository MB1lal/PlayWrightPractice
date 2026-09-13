import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports dropdown_tests.cy.js. */
test.describe('Dropdown', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Dropdown');
    await expect(page).toHaveURL(/\/dropdown/);
  });

  test('Option 2 can be selected', async ({ dropdownPage }) => {
    await dropdownPage.selectByLabel('Option 2');
    expect(await dropdownPage.selectedText()).toContain('Option 2');
  });
});
