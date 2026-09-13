import { BasePage } from '../base.page';

/** Dynamic Loading examples (/dynamic_loading/1 and /2). */
export class DynamicLoadingPage extends BasePage {
  async openExample(number: 1 | 2): Promise<void> {
    await this.page.locator(`a[href='/dynamic_loading/${number}']`).click();
  }

  async start(): Promise<void> {
    await this.page.locator('#start button').click();
  }

  /** Waits for the async render to finish and returns its text. */
  async finishText(): Promise<string> {
    const finish = this.page.locator('#finish h4');
    await finish.waitFor({ state: 'visible' });
    return ((await finish.innerText()) ?? '').trim();
  }
}
