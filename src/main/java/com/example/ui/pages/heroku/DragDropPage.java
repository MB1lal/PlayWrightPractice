package com.example.ui.pages.herokuapp;

import com.example.base.TestContext;
import com.example.ui.pages.BasePage;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * Drag and Drop page ({@code /drag_and_drop}).
 */
@Slf4j
public class DragDropPage extends BasePage {

    public DragDropPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    public void dragAToB() {
        page.locator("#column-a").dragTo(page.locator("#column-b"));
    }

    public String columnHeader(String columnId) {
        return getText(page.locator("#" + columnId + " header"), columnId + " header").trim();
    }
}
