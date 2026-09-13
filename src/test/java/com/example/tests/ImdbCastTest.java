package com.example.tests;

import com.example.support.BaseUiTest;
import com.example.ui.pages.DuckDuckGoPage;
import com.example.ui.pages.ImdbPage;
import com.example.utils.AssertionHelper;
import com.example.utils.ExcelReader;
import com.example.utils.ExcelWriter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end flow: DuckDuckGo search -&gt; IMDb title -&gt; full credits scrape
 * -&gt; Excel export (with a read-back verification).
 *
 * <p>Exports land under {@code target/test-output} with a timestamped name so
 * parallel runs never overwrite each other and committed test data is never
 * mutated.
 */
@Tag("ui")
@Tag("slow")
@DisplayName("IMDb cast scrape")
@Disabled("IMDb returns 403 to automated datacenter traffic. " +
        "Runs fine on residential networks / headed locally; re-enable with -Dtest=ImdbCastTest there.")
class ImdbCastTest extends BaseUiTest {

    @Test
    @DisplayName("Cast can be scraped from IMDb and exported to Excel")
    void castScrapedAndExported() {
        List<List<String>> input = ExcelReader.getInstance().readSheet("input");
        String searchTerm = ExcelReader.getCellValue(input, "B2");

        DuckDuckGoPage search = new DuckDuckGoPage(browser.getPage(), context);
        search.open(config.getBaseUrl());
        search.searchFor(searchTerm);
        search.openResultMentioning("IMDb");

        ImdbPage imdb = new ImdbPage(browser.getPage(), context);
        AssertionHelper.assertTrue(imdb.isLoaded(), "IMDb page should be loaded");

        imdb.scrollToText("Cast");
        imdb.openFullCredits();

        List<List<String>> cast = imdb.getCastTableData();
        AssertionHelper.assertNotEmpty(cast, "scraped cast table");
        context.setCastAndCrew(cast);

        List<List<String>> export = new ArrayList<>();
        export.add(List.of("Name", "Screen Name", "Appearances"));
        export.addAll(cast);

        Path exportFile = Paths.get("target", "test-output",
                "imdb-cast-" + System.currentTimeMillis() + ".xlsx");
        new ExcelWriter().writeToSheet(exportFile, "Series Cast", export);

        List<List<String>> reloaded = ExcelReader.getInstance().readSheet(exportFile, "Series Cast");
        assertThat(reloaded)
                .as("exported workbook should round-trip through Excel")
                .hasSize(export.size());
        assertThat(reloaded.get(0))
                .as("header row should be preserved")
                .containsExactly("Name", "Screen Name", "Appearances");
    }
}
