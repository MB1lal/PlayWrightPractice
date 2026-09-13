package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File Download page ({@code /download}).
 */
@Slf4j
public class FileDownloadPage extends BasePage {

    public FileDownloadPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /**
     * Download the first listed file into {@code target/downloads} and return its path.
     * Unlike the skipped Cypress spec, this uses the real browser download event.
     */
    public Path downloadFirstFile() {
        Download download = page.waitForDownload(() ->
                page.locator("#content a").first().click());
        Path target = Paths.get("target", "downloads", download.suggestedFilename());
        try {
            Files.createDirectories(target.getParent());
            download.saveAs(target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save download to " + target, e);
        }
        log.info("Downloaded {} ({} bytes)", target, target.toFile().length());
        return target;
    }

    public int linkCount() {
        return getElementCount(page.locator("#content a"), "download links");
    }
}
