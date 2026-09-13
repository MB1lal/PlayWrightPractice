import * as fs from 'fs';
import * as path from 'path';
import { config } from '../src/config';
import { expect, test } from '../src/fixtures';
import { getCellValue, readSheet, writeSheet } from '../src/utils/excel';

// IMDb returns 403 to automated datacenter traffic. Runs fine on residential
// networks / headed locally.
test.skip(
  'cast can be scraped from IMDb and exported to Excel',
  async ({ searchPage, imdbPage }) => {
    test.slow();
    const input = await readSheet('input');
    const term = getCellValue(input, 'B2');

    await searchPage.open(config.baseUrl);
    await searchPage.searchFor(term);
    await searchPage.openResultMentioning('IMDb');

    expect(await imdbPage.isLoaded()).toBe(true);
    await imdbPage.scrollToText('Cast');
    await imdbPage.openFullCredits();

    const cast = await imdbPage.getCastTableData();
    expect(cast.length, 'scraped cast table should not be empty').toBeGreaterThan(0);

    const exportData = [['Name', 'Screen Name', 'Appearances'], ...cast];
    const file = path.join('test-results', 'exports', `imdb-cast-${Date.now()}.xlsx`);
    fs.mkdirSync(path.dirname(file), { recursive: true });
    await writeSheet(file, 'Series Cast', exportData);

    const reloaded = await readSheet('Series Cast', file);
    expect(reloaded.length).toBe(exportData.length);
    expect(reloaded[0]).toEqual(['Name', 'Screen Name', 'Appearances']);
  },
);
