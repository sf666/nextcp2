import { deepEquals, mergeKeyedList } from './list-merge';

interface Entry {
  id: string;
  title: string;
  rating?: number | null;
}

const KEY = (entry: Entry): string => entry.id;

/** A fresh set of objects every time, so nothing passes by sharing a reference. */
function listing(): Entry[] {
  return [
    { id: 'a', title: 'Alpha' },
    { id: 'b', title: 'Beta' },
    { id: 'c', title: 'Gamma' },
  ];
}

describe('deepEquals', () => {
  it('treats a missing field, null and undefined as the same', () => {
    expect(deepEquals({ id: 'a' }, { id: 'a', rating: undefined })).toBe(true);
    expect(deepEquals({ id: 'a', rating: null }, { id: 'a' })).toBe(true);
  });

  it('does not confuse an absent rating with a zero', () => {
    expect(deepEquals({ id: 'a', rating: null }, { id: 'a', rating: 0 })).toBe(
      false,
    );
  });

  it('compares nested values and array order', () => {
    expect(deepEquals({ r: [{ x: 1 }] }, { r: [{ x: 1 }] })).toBe(true);
    expect(deepEquals({ r: [1, 2] }, { r: [2, 1] })).toBe(false);
  });
});

describe('mergeKeyedList', () => {
  it('hands back the previous array when the listing is unchanged', () => {
    const previous = listing();
    // Same content, all new objects - which is what a browse of the same container returns.
    expect(mergeKeyedList(previous, listing(), KEY)).toBe(previous);
  });

  it('keeps the identity of the entries that did not change', () => {
    const previous = listing();
    const incoming = listing();
    incoming[1] = { id: 'b', title: 'Beta', rating: 5 };

    const merged = mergeKeyedList(previous, incoming, KEY);

    expect(merged).not.toBe(previous);
    expect(merged[0]).toBe(previous[0]);
    expect(merged[1]).toBe(incoming[1]);
    expect(merged[2]).toBe(previous[2]);
  });

  it('follows the order the server sent, keeping the entries', () => {
    const previous = listing();
    const incoming = [listing()[2], listing()[0], listing()[1]];

    const merged = mergeKeyedList(previous, incoming, KEY);

    expect(merged).not.toBe(previous);
    expect(merged.map(KEY)).toEqual(['c', 'a', 'b']);
    expect(merged[0]).toBe(previous[2]);
  });

  it('notices an added and a removed entry', () => {
    const previous = listing();
    expect(
      mergeKeyedList(previous, [...listing(), { id: 'd', title: 'Delta' }], KEY)
        .length,
    ).toBe(4);
    expect(
      mergeKeyedList(previous, listing().slice(0, 2), KEY).length,
    ).toBe(2);
  });

  it('copes with an empty or absent answer', () => {
    expect(mergeKeyedList(listing(), undefined, KEY)).toEqual([]);
    expect(mergeKeyedList(undefined, listing(), KEY).length).toBe(3);
  });
});
