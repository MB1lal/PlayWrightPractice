import { defineConfig, devices } from '@playwright/test';
import { config } from './src/config';

/**
 * Playwright Test configuration — the equivalent of the Java twin's
 * ConfigManager + junit-platform.properties + BaseUiTest combined.
 *
 * Select browser with BROWSER env (chromium | firefox | webkit),
 * headed mode with --headed, workers with --workers=N.
 */
export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 4 : 4,
  reporter: [['html', { open: 'never' }], ['list']],
  timeout: 60_000,
  expect: { timeout: 10_000 },
  use: {
    baseURL: config.baseUrl,
    actionTimeout: 15_000,
    navigationTimeout: 30_000,
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    viewport: { width: config.viewportWidth, height: config.viewportHeight },
    ignoreHTTPSErrors: true,
  },
  projects: [
    { name: 'chromium', use: { ...devices['Desktop Chrome'], channel: config.browser === 'chrome' ? 'chrome' : undefined } },
    { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
    { name: 'webkit', use: { ...devices['Desktop Safari'] } },
  ],
});
