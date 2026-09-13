import * as path from 'path';
import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports upload_tests.cy.js using native file-chooser support. */
test.describe('File upload', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('File Upload');
    await expect(page).toHaveURL(/\/upload/);
  });

  test('chosen file uploads and its name is echoed back', async ({ uploadPage }) => {
    await uploadPage.upload(path.join('test-data', 'sample-upload.txt'));
    expect(await uploadPage.uploadedFileName()).toContain('sample-upload.txt');
  });
});
