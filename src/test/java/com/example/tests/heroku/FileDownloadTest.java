package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.FileDownloadPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ports {@code download_tests.cy.js} (skipped in Cypress) using Playwright's
 * download event — no extra plugins required.
 */
@Tag("ui")
@DisplayName("File download")
class FileDownloadTest extends BaseUiTest {

    private FileDownloadPage downloadPage;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("File Download");
        AssertionHelper.assertTrue(browser.url().contains("/download"),
                "Should be on download but was " + browser.url());
        downloadPage = new FileDownloadPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("A listed file downloads to disk with content")
    void fileDownloadsSuccessfully() throws Exception {
        AssertionHelper.assertTrue(downloadPage.linkCount() > 0, "Download links should be listed");

        Path downloaded = downloadPage.downloadFirstFile();

        assertThat(Files.exists(downloaded)).isTrue();
        assertThat(Files.size(downloaded)).isPositive();
    }
}
