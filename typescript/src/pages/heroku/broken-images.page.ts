import { BasePage } from '../base.page';

/** Broken Images page (/broken_images). */
export class BrokenImagesPage extends BasePage {
  /**
   * Rendered width of an image; broken images render at 0px (mirrors the
   * Cypress naturalWidth checks).
   */
  async naturalWidth(src: string): Promise<number> {
    const image = this.page.locator(`img[src='${src}']`);
    await image.waitFor();
    return image.evaluate((el: HTMLImageElement) => el.naturalWidth);
  }

  /** Wait until the image has finished loading (only for images expected to be valid). */
  async waitForLoaded(src: string): Promise<void> {
    await this.page.waitForFunction(
      (s: string) => {
        const img = document.querySelector(`img[src='${s}']`) as HTMLImageElement | null;
        return !!img && img.complete && img.naturalWidth > 0;
      },
      src,
      { timeout: 10_000 },
    );
  }
}
