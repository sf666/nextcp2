import { browseGridRowAt, layoutBrowseGrid } from './browse-grid-layout';

describe('browse grid layout', () => {
  const groups = [
    { key: 'A', items: [1, 2, 3] },
    { key: 'B', items: [4, 5] },
  ];

  it('includes headings and gaps without filling a partial row from the next group', () => {
    const rows = layoutBrowseGrid(groups, 2, 100, 16, 60);
    expect(rows.map((row) => [row.top, row.end])).toEqual([
      [0, 60], [60, 176], [176, 276], [276, 336], [336, 436],
    ]);
    expect(rows.flatMap((row) => 'items' in row ? [row.items] : [])).toEqual([[1, 2], [3], [4, 5]]);
    expect(browseGridRowAt(rows, 276)).toBe(3);
    expect(browseGridRowAt(rows, 436)).toBe(rows.length);
  });

  it('recalculates positions for a narrower grid and ignores an empty group', () => {
    const rows = layoutBrowseGrid([...groups, { key: 'C', items: [] }], 1, 100, 16, 60);
    expect(rows.at(-1)?.end).toBe(668);
    expect(browseGridRowAt(rows, 391)).toBe(3);
  });
});
