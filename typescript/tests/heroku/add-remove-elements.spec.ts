import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports add_remove_elements_tests.cy.js. */
test.describe('Add/remove elements', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Add/Remove Elements');
    await expect(page).toHaveURL(/\/add_remove_elements/);
  });

  test('added element appears on the page', async ({ addRemovePage }) => {
    await addRemovePage.addElements(1);
    expect(await addRemovePage.isDeleteVisible()).toBe(true);
  });

  test('deleted element disappears from the page', async ({ addRemovePage }) => {
    await addRemovePage.addElements(1);
    expect(await addRemovePage.isDeleteVisible()).toBe(true);

    await addRemovePage.deleteFirst();
    expect(await addRemovePage.isDeleteVisible()).toBe(false);
  });
});
