import { BasePage } from '../base.page';

/** Checkboxes page (/checkboxes). */
export class CheckboxesPage extends BasePage {
  private box(index: number) {
    return this.page.locator('#checkboxes > input').nth(index);
  }

  async toggle(index: number): Promise<void> {
    await this.box(index).click();
  }

  async isChecked(index: number): Promise<boolean> {
    return this.box(index).isChecked();
  }
}
