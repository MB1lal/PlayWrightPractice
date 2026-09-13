import * as fs from 'fs';
import * as path from 'path';
import { BasePage } from '../base.page';

/** File Download page (/download). */
export class FileDownloadPage extends BasePage {
  async linkCount(): Promise<number> {
    return this.page.locator('#content a').count();
  }

  /**
   * Download the first listed file into test-results/downloads and return
   * its path. Uses the real browser download event — no plugins needed.
   */
  async downloadFirstFile(): Promise<string> {
    const [download] = await Promise.all([
      this.page.waitForEvent('download'),
      this.page.locator('#content a').first().click(),
    ]);
    const dir = path.join('test-results', 'downloads');
    fs.mkdirSync(dir, { recursive: true });
    const target = path.join(dir, download.suggestedFilename());
    await download.saveAs(target);
    return target;
  }
}
