import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports authentication_tests.cy.js. */
test.describe('Form authentication', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Form Authentication');
    await expect(page).toHaveURL(/\/login/);
  });

  test('valid credentials log into the secure area', async ({ loginPage }) => {
    await loginPage.login('tomsmith', 'SuperSecretPassword!');
    expect(await loginPage.flashContains('You logged into a secure area!')).toBe(true);
  });

  test('login followed by logout shows the logout flash', async ({ loginPage }) => {
    await loginPage.login('tomsmith', 'SuperSecretPassword!');
    expect(await loginPage.flashContains('You logged into a secure area!')).toBe(true);

    await loginPage.logout();
    expect(await loginPage.flashContains('You logged out of the secure area!')).toBe(true);
  });

  test('invalid username shows an error flash', async ({ loginPage }) => {
    await loginPage.login('test', 'SuperSecretPassword!');
    expect(await loginPage.flashContains('Your username is invalid!')).toBe(true);
  });
});
