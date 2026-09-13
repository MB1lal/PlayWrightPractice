import { BasePage } from '../base.page';

/** Hovers page (/hovers): hovering an avatar reveals its profile caption. */
export class HoversPage extends BasePage {
  /** Hover figure `index` (zero-based) and return its revealed profile name. */
  async hoverFigure(index: number): Promise<string> {
    const figure = this.page.locator('.figure').nth(index);
    await figure.hover();
    const caption = figure.locator('.figcaption h5').first();
    await caption.waitFor({ state: 'visible' });
    return ((await caption.innerText()) ?? '').trim();
  }
}
