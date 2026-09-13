import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports js_alerts_tests.cy.js. */
test.describe('JavaScript alerts', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('JavaScript Alerts');
    await expect(page).toHaveURL(/\/javascript_alerts/);
  });

  test('JS alert text matches and success message appears', async ({ jsAlertsPage }) => {
    const message = await jsAlertsPage.clickAndHandleDialog('Click for JS Alert', (dialog) =>
      dialog.accept(),
    );
    expect(message).toBe('I am a JS Alert');
    expect(await jsAlertsPage.resultText()).toContain('You successfully clicked an alert');
  });

  test('accepting the confirm shows the Ok result', async ({ jsAlertsPage }) => {
    const message = await jsAlertsPage.clickAndHandleDialog('Click for JS Confirm', (dialog) =>
      dialog.accept(),
    );
    expect(message).toBe('I am a JS Confirm');
    expect(await jsAlertsPage.resultText()).toContain('You clicked: Ok');
  });

  test('answering the prompt echoes the entered text', async ({ jsAlertsPage }) => {
    // NOTE: the live page says "I am a JS prompt" (lowercase p).
    const message = await jsAlertsPage.clickAndHandleDialog('Click for JS Prompt', (dialog) =>
      dialog.accept('Test'),
    );
    expect(message).toBe('I am a JS prompt');
    expect(await jsAlertsPage.resultText()).toContain('You entered: Test');
  });
});
