import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports checkboxes_tests.cy.js. */
test.describe('Checkboxes', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Checkboxes');
    await expect(page).toHaveURL(/\/checkboxes/);
  });

  test('checkboxes can be selected and deselected', async ({ checkboxesPage }) => {
    await checkboxesPage.toggle(0);
    expect(await checkboxesPage.isChecked(0)).toBe(true);

    await checkboxesPage.toggle(1);
    expect(await checkboxesPage.isChecked(1)).toBe(false);
  });
});
