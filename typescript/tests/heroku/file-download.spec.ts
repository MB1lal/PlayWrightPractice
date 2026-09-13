import * as fs from 'fs';
import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports download_tests.cy.js (skipped in Cypress) via the download event. */
test.describe('File download', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('File Download');
    await expect(page).toHaveURL(/\/download/);
  });

  test('a listed file downloads to disk with content', async ({ downloadPage }) => {
    expect(await downloadPage.linkCount()).toBeGreaterThan(0);

    const downloaded = await downloadPage.downloadFirstFile();
    expect(fs.existsSync(downloaded)).toBe(true);
    expect(fs.statSync(downloaded).size).toBeGreaterThan(0);
  });
});
