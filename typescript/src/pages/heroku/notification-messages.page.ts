import { BasePage } from '../base.page';

/**
 * Notification Messages page (/notification_message).
 * The flash banner shows one of two messages at random, exactly as the
 * Cypress spec models it.
 */
export class NotificationMessagesPage extends BasePage {
  static readonly VALID_MESSAGES = ['Action successful', 'Action unsuccesful, please try again'];

  /** Current flash text with the close icon stripped (mirrors Cypress). */
  async flashText(): Promise<string> {
    const flash = this.page.locator('#flash');
    await flash.waitFor();
    return ((await flash.innerText()) ?? '').replace('×', '').trim();
  }

  async loadNewMessage(): Promise<void> {
    await this.page.locator('p > a').click();
  }
}
