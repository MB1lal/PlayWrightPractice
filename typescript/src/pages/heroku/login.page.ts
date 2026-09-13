import { BasePage } from '../base.page';

/** Form Authentication page (/login) and the secure area it guards. */
export class LoginPage extends BasePage {
  async login(user: string, password: string): Promise<void> {
    await this.page.locator('#username').fill(user);
    await this.page.locator('#password').fill(password);
    await this.page.locator('button.radius').click();
  }

  async logout(): Promise<void> {
    await this.page.locator("a[href='/logout']").click();
  }

  /** Flash banners include a close icon, so match on containment. */
  async flashContains(expected: string): Promise<boolean> {
    const text = (await this.page.locator('#flash').innerText()) ?? '';
    return text.includes(expected);
  }
}
