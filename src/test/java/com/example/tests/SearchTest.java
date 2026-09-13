package com.example.tests;

import com.example.support.BaseUiTest;
import com.example.ui.pages.DuckDuckGoPage;
import com.example.utils.AssertionHelper;
import com.example.utils.ExcelReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

/**
 * Search-engine smoke tests (DuckDuckGo — automation-friendly, no consent
 * walls or bot challenges).
 *
 * <p>Search terms come from the {@code input} sheet of {@code testData.xlsx},
 * so updating test data never requires touching code.
 */
@Tag("ui")
@DisplayName("Search")
@Disabled("Search providers bot-block datacenter IPs (Google reCAPTCHA, DDG human-challenge). " +
        "Runs fine on residential networks / headed locally; re-enable with -Dtest=SearchTest there.")
class SearchTest extends BaseUiTest {

    static Stream<String> searchTerms() {
        List<List<String>> input = ExcelReader.getInstance().readSheet("input");
        return Stream.of(
                ExcelReader.getCellValue(input, "B2"),
                ExcelReader.getCellValue(input, "B3"));
    }

    @Test
    @DisplayName("Home page loads with a visible search box")
    void homePageLoads() {
        DuckDuckGoPage search = new DuckDuckGoPage(browser.getPage(), context);
        search.open(config.getBaseUrl());

        AssertionHelper.assertTrue(search.isLoaded(), "Search box should be visible");
    }

    @ParameterizedTest(name = "search ''{0}'' returns result links")
    @MethodSource("searchTerms")
    @DisplayName("Each Excel search term returns result links")
    void searchReturnsResults(String searchTerm) {
        AssertionHelper.assertNotEmpty(searchTerm, "search term");

        DuckDuckGoPage search = new DuckDuckGoPage(browser.getPage(), context);
        search.open(config.getBaseUrl());
        search.searchFor(searchTerm);

        AssertionHelper.assertTrue(search.resultCount() > 0,
                "Search for '" + searchTerm + "' should return at least one result");
    }
}
