import { AbTestPage } from '../../src/pages/heroku/ab-test.page';
import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/**
 * Ports ab_testing_tests.cy.js. Note: with the opt-out cookie the live site
 * now deterministically renders "No A/B Test" (verified against the server).
 */
test.describe('A/B testing', { tag: '@ui' }, () => {
  test('opt-out cookie renders the control heading, stable across reload', async ({
    abTestPage,
    context,
    page,
  }) => {
    await context.addCookies([
      { name: 'optimizelyOptOut', value: 'true', domain: 'the-internet.herokuapp.com', path: '/' },
    ]);

    await abTestPage.open(config.herokuUrl);
    expect(await abTestPage.heading()).toBe('No A/B Test');

    await page.reload();
    expect(await abTestPage.heading()).toBe('No A/B Test');
  });

  test('opt-out URL raises the success alert and shows the control heading', async ({
    abTestPage,
  }) => {
    expect(await abTestPage.openOptOutUrl(config.herokuUrl)).toBe(AbTestPage.OPT_OUT_MESSAGE);
    expect(await abTestPage.heading()).toBe('No A/B Test');
  });
});
