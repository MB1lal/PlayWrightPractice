import { Page } from '@playwright/test';
import { BasePage } from '../base.page';

/** Multiple Windows page (/windows): the link opens a new tab. */
export class MultipleWindowsPage extends BasePage {
  /** Click "Click Here" and return the newly opened page. */
  async openNewWindow(): Promise<Page> {
    const [popup] = await Promise.all([
      this.page.waitForEvent('popup'),
      this.page.locator('text=Click Here').first().click(),
    ]);
    await popup.waitForLoadState();
    return popup;
  }
}
