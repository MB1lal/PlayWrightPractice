import { BasePage } from '../base.page';

/** Add/Remove Elements page (/add_remove_elements/). */
export class AddRemoveElementsPage extends BasePage {
  private readonly deleteButtons = this.page.locator('.added-manually');

  async addElements(times: number): Promise<void> {
    for (let i = 0; i < times; i++) {
      await this.page.locator("button[onclick='addElement()']").click();
    }
  }

  async deleteCount(): Promise<number> {
    return this.deleteButtons.count();
  }

  async isDeleteVisible(): Promise<boolean> {
    return (await this.deleteCount()) > 0 && (await this.deleteButtons.first().isVisible());
  }

  async deleteFirst(): Promise<void> {
    await this.deleteButtons.first().click();
  }
}
