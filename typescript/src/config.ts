import * as dotenv from 'dotenv';

dotenv.config();

function str(key: string, fallback: string): string {
  const value = process.env[key];
  return value !== undefined && value !== '' ? value : fallback;
}

function int(key: string, fallback: number): number {
  const parsed = parseInt(str(key, ''), 10);
  return Number.isNaN(parsed) ? fallback : parsed;
}

/**
 * Central configuration — mirrors the Java twin's ConfigManager.
 * Resolution order: environment variable (or .env) > hard-coded default.
 * BROWSER is consumed by playwright.config.ts to pick the project.
 */
export const config = {
  baseUrl: str('BASE_URL', 'https://duckduckgo.com'),
  herokuUrl: str('HEROKU_URL', 'https://the-internet.herokuapp.com'),
  browser: str('BROWSER', 'chromium').toLowerCase(),
  headless: str('HEADLESS', 'true') === 'true',
  slowMoMs: int('SLOW_MO_MS', 0),
  viewportWidth: int('VIEWPORT_WIDTH', 1920),
  viewportHeight: int('VIEWPORT_HEIGHT', 1080),
  testDataDir: str('TESTDATA_DIR', 'test-data'),
};
