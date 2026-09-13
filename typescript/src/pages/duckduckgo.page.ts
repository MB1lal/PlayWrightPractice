import { BasePage } from './base.page';

/**
 * DuckDuckGo search page. Used instead of Google: Google serves reCAPTCHA
 * bot-challenges to datacenter IPs (see the Java twin's SearchTest notes).
 */
export class DuckDuckGoPage extends BasePage {
  // DDG renders its search field as a TEXTAREA (role=combobox), not an <input>.
  private readonly searchBox = this.page
    .locator("#searchbox_homepage textarea, textarea[role='combobox'], input[name='q']")
    .first();

  async open(url: string): Promise<void> {
    await this.page.goto(url);
    await this.searchBox.waitFor();
  }

  async isLoaded(): Promise<boolean> {
    return this.searchBox.isVisible();
  }

  async searchFor(term: string): Promise<void> {
    await this.searchBox.fill(term);
    await this.searchBox.press('Enter');
  }

  /** Count rendered result titles (waits for the first so the count is stable). */
  async resultCount(): Promise<number> {
    await this.resultTitles().first().waitFor();
    return this.resultTitles().count();
  }

  /**
   * Open the first result whose title mentions the given text (e.g. "IMDb").
   * DDG wraps result links in redirect URLs, so match on title text, not href.
   */
  async openResultMentioning(text: string): Promise<void> {
    const result = this.resultTitles().filter({ hasText: text }).first();
    await result.scrollIntoViewIfNeeded();
    await result.click();
  }

  private resultTitles() {
    return this.page.locator("a[data-testid='result-title-a'], a.result__a");
  }
}
