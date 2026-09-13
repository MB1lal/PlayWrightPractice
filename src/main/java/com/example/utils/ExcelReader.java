package com.example.utils;

import com.example.config.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads Excel test-data files into plain {@code List<List<String>>} tables.
 *
 * <p>Streams and workbooks are always closed (try-with-resources), so repeated
 * and parallel reads never leak file handles.
 */
@Slf4j
public class ExcelReader {

    private static final ExcelReader INSTANCE = new ExcelReader();

    private final ConfigManager config = ConfigManager.getInstance();
    private final DataFormatter formatter = new DataFormatter();

    private ExcelReader() {
    }

    public static ExcelReader getInstance() {
        return INSTANCE;
    }

    /** Read a sheet from the bundled {@code testData.xlsx}. */
    public List<List<String>> readSheet(String sheetName) {
        return readSheet(Paths.get(config.getTestDataDir(), "testData.xlsx"), sheetName);
    }

    /** Read a sheet from any workbook file. */
    public List<List<String>> readSheet(Path file, String sheetName) {
        log.info("Reading sheet '{}' from {}", sheetName, file);
        try (FileInputStream input = new FileInputStream(file.toFile());
             XSSFWorkbook workbook = new XSSFWorkbook(input)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException(
                        "Sheet '" + sheetName + "' not found in " + file);
            }

            int maxCells = 0;
            for (Row row : sheet) {
                maxCells = Math.max(maxCells, row.getLastCellNum());
            }

            List<List<String>> data = new ArrayList<>();
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (int cellIndex = 0; cellIndex < maxCells; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    rowData.add(cell == null ? "" : formatter.formatCellValue(cell).trim());
                }
                data.add(rowData);
            }
            log.info("Read {} rows from sheet '{}'", data.size(), sheetName);
            return data;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read sheet '" + sheetName + "' from " + file, e);
        }
    }

    /**
     * Fetch a single cell from an in-memory table using Excel notation
     * (e.g. {@code "B2"}). Supports multi-letter columns and multi-digit rows.
     */
    public static String getCellValue(List<List<String>> data, String cellRef) {
        int[] address = parseCellRef(cellRef);
        return data.get(address[0]).get(address[1]);
    }

    /** Parse {@code "B2"} into {@code {rowIndex, columnIndex}} (both zero-based). */
    public static int[] parseCellRef(String cellRef) {
        String ref = cellRef.trim().toUpperCase();
        int split = 0;
        while (split < ref.length() && Character.isLetter(ref.charAt(split))) {
            split++;
        }
        if (split == 0 || split == ref.length()) {
            throw new IllegalArgumentException("Invalid cell reference: '" + cellRef + "'");
        }
        int column = 0;
        for (int i = 0; i < split; i++) {
            column = column * 26 + (ref.charAt(i) - 'A' + 1);
        }
        int row;
        try {
            row = Integer.parseInt(ref.substring(split));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cell reference: '" + cellRef + "'", e);
        }
        if (row < 1) {
            throw new IllegalArgumentException("Row number must be >= 1: '" + cellRef + "'");
        }
        return new int[]{row - 1, column - 1};
    }
}
