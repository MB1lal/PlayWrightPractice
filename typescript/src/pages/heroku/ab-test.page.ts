import { BasePage } from '../base.page';

/**
 * A/B Test page (/abtest): Optimizely bucketing with an opt-out cookie
 * and an opt-out URL that raises an alert.
 */
export class AbTestPage extends BasePage {
  static readonly OPT_OUT_MESSAGE = 'You have successfully opted out of Optimizely for this domain.';

  async open(baseUrl: string): Promise<void> {
    await this.page.goto(`${baseUrl}/abtest`);
  }

  async heading(): Promise<string> {
    return ((await this.page.locator('h3').innerText()) ?? '').trim();
  }

  /** Open the opt-out URL and return the alert text it raises. */
  async openOptOutUrl(baseUrl: string): Promise<string | null> {
    let alertText: string | null = null;
    this.page.once('dialog', async (dialog) => {
      alertText = dialog.message();
      await dialog.dismiss();
    });
    await this.page.goto(`${baseUrl}/abtest?optimizely_opt_out=true`);
    return alertText;
  }
}
