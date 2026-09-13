# Playwright TS Framework

TypeScript twin of [`java/`](../java/README.md) — same demo targets, same suite,
written the Playwright Test way: fixtures instead of base classes, web-first
`expect`, HTML/trace/video built into the runner.

## Prerequisites

- **Node 20+**, browsers install with `npx playwright install chromium`

## Running tests

```bash
npm install
npx playwright install chromium

npm test                          # headless Chromium, parallel
npm run test:headed               # headed mode
npm run test:firefox              # other browsers (also: test:webkit)
BROWSER=firefox npm test -- --project=firefox
npx playwright test tests/heroku/login.spec.ts   # one file
npx playwright show-report        # HTML report of the last run
npx tsc --noEmit                  # typecheck (npm run lint)
```

Copy `.env.example` to `.env` to persist `BASE_URL`, `HEROKU_URL`, `BROWSER`,
`HEADLESS`, `SLOW_MO_MS` — plain environment variables always win.

## Structure

```
typescript/
├── playwright.config.ts   # projects (chromium/firefox/webkit), retries, trace/video/screenshot policy
├── src/
│   ├── config.ts          # env/.env/defaults (mirrors Java ConfigManager)
│   ├── fixtures.ts        # per-test page-object fixtures (replaces BaseUiTest)
│   ├── pages/             # BasePage, DuckDuckGoPage, ImdbPage, heroku/ (16 pages)
│   └── utils/excel.ts     # readSheet/writeSheet/cell refs via exceljs
├── tests/                 # search, imdb, excel + heroku/ (17 specs mirroring Cypress)
└── test-data/             # testData.xlsx, sample-upload.txt
```

Reports and artifacts land in `test-results/` (traces, videos, screenshots) and
`playwright-report/` (HTML).

## Writing a new test

```ts
import { expect, test } from '../src/fixtures';
import { config } from '../src/config';

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
});
```

Add the page object to `src/pages/`, register one fixture line in `src/fixtures.ts`,
write the spec. Prefer web-first assertions (`toHaveURL`, `toContainText`) over
boolean helpers — they auto-retry.

## Notes carried over from the Java twin

- `goTo` uses exact role-name matching (`Frames` would otherwise match `Nested Frames`).
- The `<option>` selected text is read via JS (options are never "visible").
- The demo TinyMCE key is quota-exhausted (read-only editor) — iframe presence is asserted.
- `search` (3 tests) and `imdb` (1 test) are skipped: Google/DDG/IMDb bot-block
  datacenter IPs. They pass on residential networks — remove the skip there.

## Tech stack

| Library | Version |
|---|---|
| `@playwright/test` | ^1.55 (latest 1.x at install) |
| TypeScript | ^5.6 |
| `@faker-js/faker` | ^9 (replaces abandoned JavaFaker) |
| `exceljs` | ^4.4 (replaces Apache POI) |
| `dotenv` | ^16 |
