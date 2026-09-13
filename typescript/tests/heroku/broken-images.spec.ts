import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports broken_images_tests.cy.js. */
test.describe('Broken images', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Broken Images');
    await expect(page).toHaveURL(/\/broken_images/);
  });

  test('known-broken images render at zero size', async ({ brokenImagesPage }) => {
    expect(await brokenImagesPage.naturalWidth('asdf.jpg')).toBe(0);
    expect(await brokenImagesPage.naturalWidth('hjkl.jpg')).toBe(0);
  });

  test('valid image renders with non-zero size', async ({ brokenImagesPage }) => {
    await brokenImagesPage.waitForLoaded('img/avatar-blank.jpg');
    expect(await brokenImagesPage.naturalWidth('img/avatar-blank.jpg')).toBeGreaterThan(0);
  });
});
