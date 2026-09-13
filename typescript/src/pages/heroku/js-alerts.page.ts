import { BasePage } from '../base.page';

/** JavaScript Alerts page (/javascript_alerts). */
export class JsAlertsPage extends BasePage {
  /**
   * Click a button that raises a dialog; returns the dialog message.
   * The handler runs before the click resolves.
   */
  async clickAndHandleDialog(
    buttonText: string,
    handler: (dialog: import('@playwright/test').Dialog) => Promise<void>,
  ): Promise<string | null> {
    let message: string | null = null;
    this.page.once('dialog', async (dialog) => {
      message = dialog.message();
      await handler(dialog);
    });
    await this.page.locator(`text=${buttonText}`).first().click();
    return message;
  }

  async resultText(): Promise<string> {
    return ((await this.page.locator('#result').innerText()) ?? '').trim();
  }
}
