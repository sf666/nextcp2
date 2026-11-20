export interface BrowseGridGroup<T> {
  key: string;
  items: readonly T[];
}

export type BrowseGridRow<T> = {
  key: string;
  top: number;
  end: number;
} & ({ heading: string } | { items: readonly T[]; gapAfter: number });

/** Each group begins on a new grid row and contributes its own heading and gaps. */
export function layoutBrowseGrid<T>(
  groups: readonly BrowseGridGroup<T>[],
  columns: number,
  tileHeight: number,
  gap: number,
  headingHeight: number,
): BrowseGridRow<T>[] {
  const rows: BrowseGridRow<T>[] = [];
  let top = 0;
  for (const { key, items } of groups) {
    if (items.length === 0) {
      continue;
    }
    if (key) {
      rows.push({ key: 'heading:' + key, heading: key, top, end: top + headingHeight });
      top += headingHeight;
    }
    for (let index = 0; index < items.length; index += columns) {
      const gapAfter = index + columns < items.length ? gap : 0;
      const end = top + tileHeight + gapAfter;
      rows.push({
        key: `tiles:${key}:${index}`,
        items: items.slice(index, index + columns),
        gapAfter,
        top,
        end,
      });
      top = end;
    }
  }
  return rows;
}

/** Finds the first row intersecting or following an offset. The end may be beyond the grid. */
export function browseGridRowAt(rows: readonly BrowseGridRow<unknown>[], offset: number): number {
  let first = 0;
  let last = rows.length;
  while (first < last) {
    const middle = Math.floor((first + last) / 2);
    if (rows[middle].end <= offset) {
      first = middle + 1;
    } else {
      last = middle;
    }
  }
  return first;
}
