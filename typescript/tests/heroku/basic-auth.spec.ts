import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/**
 * Ports basic_auth_tests.cy.js. Credentials are installed into a fresh
 * browser context (the equivalent of Cypress's `auth` visit option).
 */
test.describe('Basic auth', { tag: '@ui' }, () => {
  test('valid credentials show the congratulations page', async ({ browser }) => {
    const context = await browser.newContext({
      httpCredentials: { username: 'admin', password: 'admin' },
    });
    const page = await context.newPage();
    await page.goto(`${config.herokuUrl}/basic_auth`);
    await expect(page.locator('body')).toContainText(
      'Congratulations! You must have the proper credentials.',
    );
    await context.close();
  });

  test('invalid credentials show the not-authorized page', async ({ browser }) => {
    const context = await browser.newContext({
      httpCredentials: { username: 'test', password: 'test' },
    });
    const page = await context.newPage();
    await page.goto(`${config.herokuUrl}/basic_auth`);
    await expect(page.locator('body')).toContainText('Not authorized');
    await context.close();
  });
});
