import { faker } from '@faker-js/faker';
import * as os from 'os';
import * as path from 'path';
import { expect, test } from '../src/fixtures';
import { getCellValue, parseCellRef, readSheet, writeSheet } from '../src/utils/excel';

/** Fast, hermetic tests for the Excel utilities (no browser needed). */
test.describe('Excel utilities', () => {
  test('write then read round-trips generated data unchanged', async () => {
    const expected: string[][] = [['Name', 'Screen Name', 'Appearances']];
    for (let i = 0; i < 10; i++) {
      expected.push([
        faker.person.fullName(),
        faker.person.firstName(),
        `${faker.number.int({ min: 1, max: 24 })} episodes`,
      ]);
    }

    const file = path.join(os.tmpdir(), `roundtrip-${Date.now()}.xlsx`);
    await writeSheet(file, 'Series Cast', expected);
    expect(await readSheet('Series Cast', file)).toEqual(expected);
  });

  test('writing twice to the same sheet replaces stale rows', async () => {
    const file = path.join(os.tmpdir(), `rewrite-${Date.now()}.xlsx`);
    await writeSheet(file, 'data', [
      ['a', 'b', 'c'],
      ['d', 'e', 'f'],
      ['g', 'h', 'i'],
    ]);
    await writeSheet(file, 'data', [['only']]);
    expect(await readSheet('data', file)).toEqual([['only']]);
  });

  for (const [ref, row, column] of [
    ['A1', 0, 0],
    ['B2', 1, 1],
    ['C3', 2, 2],
    ['B12', 11, 1],
    ['AA10', 9, 26],
    ['b2', 1, 1],
  ] as const) {
    test(`cell reference ${ref} parses to row ${row}, column ${column}`, () => {
      expect(parseCellRef(ref)).toEqual([row, column]);
    });
  }

  test('invalid cell references are rejected', () => {
    expect(() => parseCellRef('nope-no-digits')).toThrow();
    expect(() => parseCellRef('12')).toThrow();
  });

  test('bundled test-data workbook exposes the search inputs', async () => {
    const input = await readSheet('input');
    expect(getCellValue(input, 'B2')).not.toBe('');
    expect(getCellValue(input, 'B3')).not.toBe('');
  });
});
