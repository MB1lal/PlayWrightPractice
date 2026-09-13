import { Locator, Page } from '@playwright/test';

/**
 * Base class for all page objects — the equivalent of the Java twin's BasePage.
 * Thin wrappers only: Playwright's auto-waiting + web-first assertions in
 * specs do the heavy lifting.
 */
export abstract class BasePage {
  constructor(
    protected readonly page: Page,
  ) {}

  protected async click(locator: Locator, name: string): Promise<void> {
    await locator.click();
  }

  protected async fill(locator: Locator, text: string): Promise<void> {
    await locator.fill(text);
  }

  async title(): Promise<string> {
    return this.page.title();
  }

  url(): string {
    return this.page.url();
  }
}
