import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports drag_drop_tests.cy.js. */
test.describe('Drag and drop', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Drag and Drop');
    await expect(page).toHaveURL(/\/drag_and_drop/);
  });

  test('column A drops onto column B and they swap', async ({ dragDropPage }) => {
    await dragDropPage.dragAToB();
    expect(await dragDropPage.columnHeader('column-a')).toBe('B');
    expect(await dragDropPage.columnHeader('column-b')).toBe('A');
  });
});
