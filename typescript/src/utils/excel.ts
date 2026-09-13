import ExcelJS from 'exceljs';
import * as path from 'path';
import { config } from '../config';

export type Table = string[][];

/**
 * Excel test-data helpers — the equivalent of the Java twin's
 * ExcelReader/ExcelWriter. Reads workbook sheets into plain string tables
 * and writes tables back out (replacing the target sheet).
 */
export async function readSheet(sheetName: string, file?: string): Promise<Table> {
  const filePath = file ?? path.join(config.testDataDir, 'testData.xlsx');
  const workbook = new ExcelJS.Workbook();
  await workbook.xlsx.readFile(filePath);
  const sheet =
    workbook.getWorksheet(sheetName) ??
    workbook.worksheets.find((s) => s.name.toLowerCase() === sheetName.toLowerCase());
  if (!sheet) {
    throw new Error(`Sheet '${sheetName}' not found in ${filePath}`);
  }
  const table: Table = [];
  sheet.eachRow({ includeEmpty: false }, (row) => {
    const values = (row.values ?? []) as ExcelJS.CellValue[];
    // exceljs rows are 1-based; index 0 is always empty.
    const cells: string[] = [];
    for (let i = 1; i < values.length; i++) {
      cells.push(cellToString(values[i]));
    }
    table.push(cells);
  });
  return table;
}

export async function writeSheet(file: string, sheetName: string, data: Table): Promise<void> {
  const workbook = new ExcelJS.Workbook();
  try {
    await workbook.xlsx.readFile(file);
  } catch {
    // New file — start from an empty workbook.
  }
  const existing = workbook.getWorksheet(sheetName);
  if (existing) {
    workbook.removeWorksheet(existing.id);
  }
  const sheet = workbook.addWorksheet(sheetName);
  data.forEach((row) => sheet.addRow(row));
  await workbook.xlsx.writeFile(file);
}

function cellToString(value: ExcelJS.CellValue): string {
  if (value === null || value === undefined) return '';
  if (typeof value === 'object') {
    if ('text' in value && typeof (value as { text: unknown }).text === 'string') {
      return (value as { text: string }).text.trim();
    }
    if ('result' in value) return cellToString((value as { result: ExcelJS.CellValue }).result);
    if (value instanceof Date) return value.toISOString();
  }
  return String(value).trim();
}

/**
 * Fetch a cell from an in-memory table using Excel notation (e.g. "B2").
 * Supports multi-letter columns and multi-digit rows.
 */
export function getCellValue(data: Table, cellRef: string): string {
  const [row, column] = parseCellRef(cellRef);
  return data[row][column];
}

/** Parse "B2" into zero-based [row, column]. */
export function parseCellRef(cellRef: string): [number, number] {
  const match = /^([A-Za-z]+)(\d+)$/.exec(cellRef.trim());
  if (!match) {
    throw new Error(`Invalid cell reference: '${cellRef}'`);
  }
  const [, letters, digits] = match;
  let column = 0;
  for (const ch of letters.toUpperCase()) {
    column = column * 26 + (ch.charCodeAt(0) - 'A'.charCodeAt(0) + 1);
  }
  const row = parseInt(digits, 10);
  if (row < 1) {
    throw new Error(`Row number must be >= 1: '${cellRef}'`);
  }
  return [row - 1, column - 1];
}
