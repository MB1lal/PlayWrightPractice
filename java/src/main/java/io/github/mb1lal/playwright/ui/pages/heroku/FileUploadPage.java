package io.github.mb1lal.playwright.ui.pages.herokuapp;

import io.github.mb1lal.playwright.base.TestContext;
import io.github.mb1lal.playwright.ui.pages.BasePage;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

/**
 * File Upload page ({@code /upload}).
 */
@Slf4j
public class FileUploadPage extends BasePage {

    public FileUploadPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    public void upload(Path file) {
        log.info("Uploading {}", file);
        // NOTE: '#file-upload' — a bare input[type='file'] also matches the
        // page's hidden Dropzone input and trips strict-mode checks.
        page.locator("#file-upload").setInputFiles(file);
        click(page.locator("#file-submit"), "Upload button");
        page.waitForLoadState();
    }

    public void assertUploadedFile(String fileName) {
        assertContainsText(page.locator("#uploaded-files"), fileName, "uploaded file name");
    }
}
