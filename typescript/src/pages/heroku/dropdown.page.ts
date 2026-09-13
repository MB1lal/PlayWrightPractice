import { BasePage } from '../base.page';

/** Dropdown page (/dropdown). */
export class DropdownPage extends BasePage {
  async selectByLabel(label: string): Promise<void> {
    await this.page.locator('#dropdown').selectOption({ label });
  }

  /**
   * Selected option text, read via JS — <option> elements are never
   * "visible", so visibility-based assertions cannot be used here.
   */
  async selectedText(): Promise<string> {
    return this.page.locator('#dropdown').evaluate(
      (el: HTMLSelectElement) => el.selectedOptions[0]?.text ?? '',
    );
  }
}
