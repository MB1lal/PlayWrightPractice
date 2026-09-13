import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports dynamic_loading_tests.cy.js. */
test.describe('Dynamic loading', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Dynamic Loading');
    await expect(page).toHaveURL(/\/dynamic_loading/);
  });

  test('hidden element renders after Start (example 1)', async ({ dynamicLoadingPage, page }) => {
    await dynamicLoadingPage.openExample(1);
    await expect(page).toHaveURL(/dynamic_loading\/1/);

    await dynamicLoadingPage.start();
    expect(await dynamicLoadingPage.finishText()).toBe('Hello World!');
  });

  test('newly created element renders after Start (example 2)', async ({
    dynamicLoadingPage,
    page,
  }) => {
    await dynamicLoadingPage.openExample(2);
    await expect(page).toHaveURL(/dynamic_loading\/2/);

    await dynamicLoadingPage.start();
    expect(await dynamicLoadingPage.finishText()).toBe('Hello World!');
  });
});
