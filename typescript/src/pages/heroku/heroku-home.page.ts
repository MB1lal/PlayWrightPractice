import { BasePage } from '../base.page';

/**
 * Landing page of the-internet.herokuapp.com.
 * Mirrors `navigateToXPage` from the Cypress support commands.
 */
export class HerokuHomePage extends BasePage {
  async open(baseUrl: string): Promise<void> {
    await this.page.goto(baseUrl);
  }

  /**
   * Follow the homepage link with the given accessible name.
   * Exact matching is required: role-name lookup is substring-based, so
   * "Frames" would otherwise also match "Nested Frames".
   */
  async goTo(linkName: string): Promise<void> {
    await this.page.getByRole('link', { name: linkName, exact: true }).click();
  }
}
