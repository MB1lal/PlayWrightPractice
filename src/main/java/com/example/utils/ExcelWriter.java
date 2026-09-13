package com.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Writes in-memory tables to Excel workbooks.
 *
 * <p>Unlike the legacy writer, this creates missing files/directories, replaces
 * the target sheet (no stale rows), and always closes its streams. Export
 * files belong under {@code target/} so committed test data is never mutated.
 */
@Slf4j
public class ExcelWriter {

    /** Write {@code data} to {@code sheetName} in {@code file}, replacing that sheet. */
    public void writeToSheet(Path file, String sheetName, List<List<String>> data) {
        log.info("Writing {} rows to sheet '{}' in {}", data.size(), sheetName, file);
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }

            XSSFWorkbook workbook;
            if (Files.exists(file)) {
                try (InputStream input = Files.newInputStream(file)) {
                    workbook = new XSSFWorkbook(input);
                }
            } else {
                workbook = new XSSFWorkbook();
            }

            try (workbook) {
                int existing = workbook.getSheetIndex(sheetName);
                if (existing >= 0) {
                    workbook.removeSheetAt(existing);
                }
                XSSFSheet sheet = workbook.createSheet(sheetName);

                for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                    var row = sheet.createRow(rowIndex);
                    List<String> rowData = data.get(rowIndex);
                    for (int cellIndex = 0; cellIndex < rowData.size(); cellIndex++) {
                        row.createCell(cellIndex).setCellValue(rowData.get(cellIndex));
                    }
                }

                try (OutputStream output = Files.newOutputStream(file)) {
                    workbook.write(output);
                }
            }
            log.info("Excel export complete: {}", file);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write sheet '" + sheetName + "' to " + file, e);
        }
    }
}
