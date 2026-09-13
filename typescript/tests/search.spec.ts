import { config } from '../src/config';
import { expect, test } from '../src/fixtures';
import { getCellValue, readSheet } from '../src/utils/excel';

// Search providers bot-block datacenter IPs (Google reCAPTCHA, DDG
// human-challenge). Runs fine on residential networks / headed locally.
test.describe.skip('Search', { tag: '@ui' }, () => {
  test('home page loads with a visible search box', async ({ searchPage }) => {
    await searchPage.open(config.baseUrl);
    expect(await searchPage.isLoaded()).toBe(true);
  });

  for (const cell of ['B2', 'B3']) {
    test(`search term from Excel (${cell}) returns result links`, async ({ searchPage }) => {
      const input = await readSheet('input');
      const term = getCellValue(input, cell);
      expect(term, `cell ${cell} should not be empty`).not.toBe('');

      await searchPage.open(config.baseUrl);
      await searchPage.searchFor(term);
      expect(await searchPage.resultCount()).toBeGreaterThan(0);
    });
  }
});
