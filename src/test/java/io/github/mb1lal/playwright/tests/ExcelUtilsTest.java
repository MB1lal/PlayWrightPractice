package io.github.mb1lal.playwright.tests;

import io.github.mb1lal.playwright.utils.ExcelReader;
import io.github.mb1lal.playwright.utils.ExcelWriter;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Fast, hermetic tests for the Excel utilities (no browser needed).
 * Doubles as the Datafaker usage example for generating test data.
 */
@DisplayName("Excel utilities")
class ExcelUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Write then read round-trips generated data unchanged")
    void writeThenReadRoundTrips() {
        Faker faker = new Faker();
        List<List<String>> expected = new ArrayList<>();
        expected.add(List.of("Name", "Screen Name", "Appearances"));
        for (int i = 0; i < 10; i++) {
            expected.add(List.of(
                    faker.name().fullName(),
                    faker.funnyName().name(),
                    faker.number().numberBetween(1, 24) + " episodes"));
        }

        Path file = tempDir.resolve("roundtrip.xlsx");
        new ExcelWriter().writeToSheet(file, "Series Cast", expected);

        List<List<String>> actual = ExcelReader.getInstance().readSheet(file, "Series Cast");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Writing twice to the same sheet replaces stale rows")
    void rewriteReplacesStaleRows() {
        Path file = tempDir.resolve("rewrite.xlsx");
        ExcelWriter writer = new ExcelWriter();

        List<List<String>> wide = List.of(
                List.of("a", "b", "c"),
                List.of("d", "e", "f"),
                List.of("g", "h", "i"));
        writer.writeToSheet(file, "data", wide);

        List<List<String>> narrow = List.of(List.of("only"));
        writer.writeToSheet(file, "data", narrow);

        assertThat(ExcelReader.getInstance().readSheet(file, "data")).isEqualTo(narrow);
    }

    @ParameterizedTest(name = "{0} -> row {1}, column {2}")
    @CsvSource({"A1, 0, 0", "B2, 1, 1", "C3, 2, 2", "B12, 11, 1", "AA10, 9, 26", "b2, 1, 1"})
    @DisplayName("Cell references parse to zero-based coordinates")
    void cellReferencesParse(String ref, int row, int column) {
        assertThat(ExcelReader.parseCellRef(ref)).containsExactly(row, column);
    }

    @Test
    @DisplayName("Invalid cell references are rejected")
    void invalidCellReferencesRejected() {
        assertThatThrownBy(() -> ExcelReader.parseCellRef("nope-no-digits"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ExcelReader.parseCellRef("12"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Bundled test-data workbook exposes the Google search inputs")
    void bundledInputSheetIsReadable() {
        List<List<String>> input = ExcelReader.getInstance().readSheet("input");
        assertThat(ExcelReader.getCellValue(input, "B2")).isNotBlank();
        assertThat(ExcelReader.getCellValue(input, "B3")).isNotBlank();
    }
}
