import { config } from '../../src/config';
import { expect, test } from '../../src/fixtures';

/** Ports frames_tests.cy.js (nested frames + iframe presence). */
test.describe('Frames', { tag: '@ui' }, () => {
  test.beforeEach(async ({ herokuHome, page }) => {
    await herokuHome.open(config.herokuUrl);
    await herokuHome.goTo('Frames');
    await expect(page).toHaveURL(/\/frames/);
  });

  test('nested frames expose the expected body text', async ({ framesPage, page }) => {
    await framesPage.openNestedFrames();
    await expect(page).toHaveURL(/\/nested_frames/);

    expect(await framesPage.nestedFrameText('left')).toContain('LEFT');
    expect(await framesPage.nestedFrameText('middle')).toContain('MIDDLE');
    expect(await framesPage.nestedFrameText('right')).toContain('RIGHT');
    expect(await framesPage.nestedFrameText('bottom')).toContain('BOTTOM');
  });

  test('iframe editor is present on the page', async ({ framesPage, page }) => {
    // NOTE: the demo's TinyMCE cloud key is out of editor loads, so the
    // editor is read-only with no content — presence is what's asserted.
    await framesPage.openIframeEditor();
    await expect(page).toHaveURL(/\/iframe/);

    await framesPage.assertEditorPresent();
  });
});
