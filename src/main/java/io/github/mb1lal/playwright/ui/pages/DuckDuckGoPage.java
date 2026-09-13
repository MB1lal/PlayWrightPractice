package io.github.mb1lal.playwright.ui.pages;

import io.github.mb1lal.playwright.base.TestContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

/**
 * DuckDuckGo search page.
 *
 * <p>Used instead of Google for the search demos: Google serves reCAPTCHA
 * bot-challenges to datacenter IPs, while DuckDuckGo is automation-friendly
 * and has no blocking consent dialog.
 */
@Slf4j
public class DuckDuckGoPage extends BasePage {

    private final Locator searchBox;

    public DuckDuckGoPage(Page page, TestContext testContext) {
        super(page, testContext);
        // NOTE: DDG renders its search field as a TEXTAREA (role=combobox),
        // not an <input>. Union selector covers classic and current markup.
        this.searchBox = page.locator(
                "#searchbox_homepage textarea, textarea[role='combobox'], input[name='q']").first();
    }

    public void open(String url) {
        log.info("Opening DuckDuckGo at {}", url);
        page.navigate(url);
        page.waitForLoadState();
        searchBox.waitFor();
    }

    public boolean isLoaded() {
        return isVisible(searchBox, "DuckDuckGo search box");
    }

    /** Search for a term by filling the box and pressing Enter. */
    public void searchFor(String term) {
        log.info("Searching DuckDuckGo for '{}'", term);
        fill(searchBox, term, "DuckDuckGo search box");
        searchBox.press("Enter");
        page.waitForLoadState();
    }

    /**
     * Number of rendered result titles. Waits for the first result so the
     * count is never taken on a half-loaded page.
     */
    public int resultCount() {
        resultTitles().first().waitFor();
        int count = resultTitles().count();
        log.debug("DuckDuckGo result count: {}", count);
        return count;
    }

    /**
     * Open the first result whose title mentions the given text (e.g. "IMDb").
     * DuckDuckGo wraps result links in redirect URLs, so results are matched
     * by title text rather than href.
     */
    public void openResultMentioning(String text) {
        log.info("Opening search result mentioning '{}'", text);
        Locator result = resultTitles()
                .filter(new Locator.FilterOptions().setHasText(text)).first();
        scrollIntoView(result, "Search result mentioning '" + text + "'");
        click(result, "Search result mentioning '" + text + "'");
        page.waitForLoadState();
    }

    private Locator resultTitles() {
        return page.locator("a[data-testid='result-title-a'], a.result__a");
    }
}
