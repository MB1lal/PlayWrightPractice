import { BasePage } from '../base.page';

/** Context Menu page (/context_menu): right-clicking the hot-spot raises a JS alert. */
export class ContextMenuPage extends BasePage {
  /** Right-click the hot-spot and return the resulting alert text. */
  async rightClickHotSpot(): Promise<string | null> {
    let alertText: string | null = null;
    this.page.once('dialog', async (dialog) => {
      alertText = dialog.message();
      await dialog.dismiss();
    });
    await this.page.locator('#hot-spot').click({ button: 'right' });
    return alertText;
  }
}
