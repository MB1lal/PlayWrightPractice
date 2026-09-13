package com.example.tests.herokuapp;

import com.example.support.BaseUiTest;
import com.example.ui.pages.herokuapp.FileUploadPage;
import com.example.ui.pages.herokuapp.HerokuHomePage;
import com.example.utils.AssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * Ports {@code upload_tests.cy.js} using Playwright's native file chooser support.
 */
@Tag("ui")
@DisplayName("File upload")
class FileUploadTest extends BaseUiTest {

    private static final String FIXTURE = "sample-upload.txt";

    private FileUploadPage uploadPage;

    @BeforeEach
    void navigateToPage() {
        HerokuHomePage home = new HerokuHomePage(browser.getPage(), context);
        home.open(config.getHerokuUrl());
        home.goTo("File Upload");
        AssertionHelper.assertTrue(browser.url().contains("/upload"),
                "Should be on upload but was " + browser.url());
        uploadPage = new FileUploadPage(browser.getPage(), context);
    }

    @Test
    @DisplayName("Chosen file uploads and its name is echoed back")
    void fileUploadsSuccessfully() {
        uploadPage.upload(Paths.get("src/test/resources/fixtures", FIXTURE));
        uploadPage.assertUploadedFile(FIXTURE);
    }
}
