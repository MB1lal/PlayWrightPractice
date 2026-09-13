import { BasePage } from '../base.page';

/** Drag and Drop page (/drag_and_drop). */
export class DragDropPage extends BasePage {
  async dragAToB(): Promise<void> {
    await this.page.locator('#column-a').dragTo(this.page.locator('#column-b'));
  }

  async columnHeader(columnId: 'column-a' | 'column-b'): Promise<string> {
    return ((await this.page.locator(`#${columnId} header`).innerText()) ?? '').trim();
  }
}
