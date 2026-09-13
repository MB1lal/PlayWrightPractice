import { BasePage } from './base.page';

/** IMDb title page (full credits view). */
export class ImdbPage extends BasePage {
  async isLoaded(): Promise<boolean> {
    const onImdb = this.page.url().includes('imdb.com');
    const headerVisible = await this.page.locator('header').first().isVisible();
    return onImdb && headerVisible;
  }

  async scrollToText(text: string): Promise<void> {
    await this.page.locator(`text=${text}`).first().scrollIntoViewIfNeeded();
  }

  /**
   * Open the full-credits view, trying known entry-point labels in order —
   * IMDb relabels these periodically.
   */
  async openFullCredits(): Promise<void> {
    for (const candidate of ['All cast & crew', 'Full cast & crew', 'See all']) {
      const target = this.page.locator(`text=${candidate}`).first();
      try {
        await target.waitFor({ timeout: 5_000 });
        if (await target.isVisible()) {
          await target.click();
          return;
        }
      } catch {
        // Try the next label.
      }
    }
  }

  /** Scrape the full-credits cast table into rows of cell values. */
  async getCastTableData(): Promise<string[][]> {
    let rows = this.page.locator('#fullcredits_content table.cast_list tr');
    if ((await rows.count()) === 0) {
      rows = this.page.locator('table.cast_list tr');
    }
    const cast: string[][] = [];
    for (let i = 0; i < (await rows.count()); i++) {
      const rowText = ((await rows.nth(i).innerText()) ?? '').trim();
      if (!rowText) continue;
      const cells = rowText
        .split(/[\t\n]/)
        .map((c) => c.trim())
        .filter((c) => c !== '' && c !== '...')
        .slice(0, 4);
      if (cells.length > 0) cast.push(cells);
    }
    return cast;
  }
}
