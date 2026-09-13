import { BasePage } from '../base.page';

/** Frames hub (/frames) with nested-frames and iframe-editor support. */
export class FramesPage extends BasePage {
  async openNestedFrames(): Promise<void> {
    await this.page.getByRole('link', { name: 'Nested Frames', exact: true }).click();
  }

  async openIframeEditor(): Promise<void> {
    await this.page.getByRole('link', { name: 'iFrame', exact: true }).click();
  }

  /** Body text of a nested frame: 'left' | 'middle' | 'right' | 'bottom'. */
  async nestedFrameText(position: 'left' | 'middle' | 'right' | 'bottom'): Promise<string> {
    const text =
      position === 'bottom'
        ? await this.page.frameLocator("frame[src='/frame_bottom']").locator('body').innerText()
        : await this.page
            .frameLocator("frame[src='/frame_top']")
            .frameLocator(`frame[src='/frame_${position}']`)
            .locator('body')
            .innerText();
    return (text ?? '').trim();
  }

  /**
   * Assert the TinyMCE iframe is present and visible.
   * NOTE: the demo site's TinyMCE cloud key is out of editor loads, so the
   * editor renders read-only with no content — presence is what's asserted.
   */
  async assertEditorPresent(): Promise<void> {
    await this.page.locator('#mce_0_ifr').waitFor({ state: 'visible' });
  }
}
