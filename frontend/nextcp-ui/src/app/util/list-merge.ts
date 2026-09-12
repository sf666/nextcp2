/**
 * Keeping references across a browse that re-reads what is already on screen.
 *
 * A refresh usually answers with the entries that are already displayed. Setting that answer as it
 * arrives is a new array of new objects: the signal notifies, every computed over it runs again,
 * the grid re-groups and re-measures and every tile receives a new input object - a full list
 * rebuild for data that did not change, which is what the user sees flicker. Handing back the
 * references that still stand for the same content makes the unchanged case cost nothing.
 */

/**
 * Structural comparison of two browse entries.
 *
 * A DTO field the backend left out and one it sent as null mean the same thing here, so both count
 * as equal to a missing one - otherwise an entry would look changed on every browse.
 */
export function deepEquals(a: unknown, b: unknown): boolean {
  if (a === b) {
    return true;
  }
  if (a == null || b == null) {
    return a == null && b == null;
  }
  if (typeof a !== 'object' || typeof b !== 'object') {
    return false;
  }
  if (Array.isArray(a) || Array.isArray(b)) {
    if (!Array.isArray(a) || !Array.isArray(b) || a.length !== b.length) {
      return false;
    }
    return a.every((entry, index) => deepEquals(entry, b[index]));
  }
  const left = a as Record<string, unknown>;
  const right = b as Record<string, unknown>;
  for (const key of new Set([...Object.keys(left), ...Object.keys(right)])) {
    if (!deepEquals(left[key], right[key])) {
      return false;
    }
  }
  return true;
}

/**
 * The incoming listing, with every entry that came back unchanged represented by the object already
 * held - and the previous array itself when the whole listing is unchanged, so the signal holding it
 * stays quiet and nothing downstream recomputes.
 *
 * Where the listing did change, the result follows the order the media server sent: it decides what
 * the listing looks like, not what happened to be on screen.
 */
export function mergeKeyedList<T>(
  previous: T[] | undefined,
  incoming: T[] | undefined,
  keyOf: (entry: T) => string,
): T[] {
  const before = previous ?? [];
  const next = incoming ?? [];
  const byKey = new Map<string, T>();
  for (const entry of before) {
    byKey.set(keyOf(entry), entry);
  }
  // A navigation to another container matches no key at all, so nothing is compared and the cost is
  // one map. Only a refresh of the same listing does the work of comparing.
  let changed = next.length !== before.length;
  const merged = next.map((entry, index) => {
    const kept = byKey.get(keyOf(entry));
    if (kept !== undefined && deepEquals(kept, entry)) {
      if (before[index] !== kept) {
        // Same entry, different place: the listing was reordered.
        changed = true;
      }
      return kept;
    }
    changed = true;
    return entry;
  });
  return changed ? merged : before;
}

/**
 * The incoming page with its unchanged entries replaced by the objects held before the refresh
 * started.
 *
 * Every page after the first appends to a list the first page has already replaced, so there is
 * nothing left to compare against - hence the snapshot taken when the refresh began. The array is
 * new either way while paging, but a tile whose entry kept its identity does not re-render.
 */
export function reuseKeyedEntries<T>(
  incoming: T[] | undefined,
  reusable: ReadonlyMap<string, unknown> | undefined,
  keyOf: (entry: T) => string,
): T[] {
  const next = incoming ?? [];
  if (!reusable?.size) {
    return next;
  }
  return next.map((entry) => {
    const kept = reusable.get(keyOf(entry)) as T | undefined;
    return kept !== undefined && deepEquals(kept, entry) ? kept : entry;
  });
}
