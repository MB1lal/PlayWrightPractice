import { BasePage } from '../base.page';

/** File Upload page (/upload). */
export class FileUploadPage extends BasePage {
  async upload(filePath: string): Promise<void> {
    // NOTE: '#file-upload' — a bare input[type='file'] also matches the
    // page's hidden Dropzone input and trips strict-mode checks.
    await this.page.locator('#file-upload').setInputFiles(filePath);
    await this.page.locator('#file-submit').click();
  }

  async uploadedFileName(): Promise<string> {
    return ((await this.page.locator('#uploaded-files').innerText()) ?? '').trim();
  }
}
