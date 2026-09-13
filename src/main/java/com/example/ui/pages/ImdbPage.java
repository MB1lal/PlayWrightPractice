package com.example.ui.pages;

import com.example.base.TestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * IMDb title page (full credits view).
 */
@Slf4j
public class ImdbPage extends BasePage {

    public ImdbPage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    /** The page is considered loaded when we are on imdb.com with a visible header. */
    public boolean isLoaded() {
        boolean onImdb = page.url().contains("imdb.com");
        boolean headerVisible = page.locator("header").first().isVisible();
        log.debug("IMDb loaded check: onImdb={}, headerVisible={}", onImdb, headerVisible);
        return onImdb && headerVisible;
    }

    public void scrollToText(String elementText) {
        log.info("Scrolling to text '{}'", elementText);
        Locator target = page.locator("text=" + elementText).first();
        scrollIntoView(target, elementText);
    }

    public void clickText(String elementText) {
        log.info("Clicking text '{}'", elementText);
        Locator target = page.locator("text=" + elementText).first();
        click(target, elementText);
        page.waitForLoadState();
    }

    /**
     * Open the full-credits view, trying known entry-point labels in order.
     * IMDb relabels these periodically, so a missing label falls through to
     * the next candidate instead of failing immediately.
     */
    public void openFullCredits() {
        String[] candidates = {"All cast & crew", "Full cast & crew", "See all"};
        for (String candidate : candidates) {
            Locator target = page.locator("text=" + candidate).first();
            try {
                target.waitFor(new Locator.WaitForOptions().setTimeout(5_000));
                if (target.isVisible()) {
                    click(target, candidate);
                    page.waitForLoadState();
                    log.info("Opened full credits via '{}'", candidate);
                    return;
                }
            } catch (Exception e) {
                log.debug("Full-credits entry '{}' not present, trying next", candidate);
            }
        }
        log.warn("No full-credits entry point found; scraping the current page instead");
    }

    /**
     * Scrape the full-credits cast table into rows of cell values.
     * Rows that cannot be parsed are skipped so one bad row never fails the scrape.
     */
    public List<List<String>> getCastTableData() {
        log.info("Scraping IMDb cast table");
        List<List<String>> castTableData = new ArrayList<>();
        Locator rows = page.locator("#fullcredits_content table.cast_list tr");
        int count = getElementCount(rows, "cast table rows");

        for (int i = 0; i < count; i++) {
            String rowText = rows.nth(i).innerText().trim();
            if (rowText.isEmpty()) {
                continue;
            }
            List<String> cells = new ArrayList<>();
            try (Scanner scanner = new Scanner(rowText).useDelimiter("[\\t\\n]")) {
                while (scanner.hasNext() && cells.size() < 4) {
                    String cell = scanner.next().trim();
                    if (!cell.isEmpty() && !cell.equals("...")) {
                        cells.add(cell);
                    }
                }
            }
            if (!cells.isEmpty()) {
                castTableData.add(cells);
            }
        }
        log.info("Scraped {} cast rows", castTableData.size());
        return castTableData;
    }
}
