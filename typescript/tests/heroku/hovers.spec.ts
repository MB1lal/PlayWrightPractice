import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports hover_tests.cy.js. */
test.describe('Hovers', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Hovers');
    await expect(page).toHaveURL(/\/hovers/);
  });

  test('each avatar reveals its profile name on hover', async ({ hoversPage }) => {
    expect(await hoversPage.hoverFigure(0)).toContain('name: user1');
    expect(await hoversPage.hoverFigure(1)).toContain('name: user2');
    expect(await hoversPage.hoverFigure(2)).toContain('name: user3');
  });
});
