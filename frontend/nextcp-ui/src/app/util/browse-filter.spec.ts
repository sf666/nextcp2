import {
  BrowseFilterMemory,
  BrowseFilterState,
  filterContainers,
  filterMusicItems,
  isUnfiltered,
  splitGenres,
  UNFILTERED,
} from './browse-filter';
import { DtoGeneratorService } from './dto-generator.service';

describe('BrowseFilterMemory', () => {
  const rated4Plus: BrowseFilterState = { ...UNFILTERED, rating: '4' };

  it('starts a listing it has not seen before unfiltered', () => {
    const memory = new BrowseFilterMemory();

    expect(memory.switchTo('albums', UNFILTERED)).toEqual(UNFILTERED);
    expect(memory.switchTo('album-17', rated4Plus)).toEqual(UNFILTERED);
  });

  it('reports nothing to change while the same listing stays on screen', () => {
    const memory = new BrowseFilterMemory();
    memory.switchTo('albums', UNFILTERED);

    // Paging in more of the same listing must not touch the filters.
    expect(memory.switchTo('albums', rated4Plus)).toBeUndefined();
  });

  it('brings a narrowing back when its listing returns', () => {
    const memory = new BrowseFilterMemory();
    memory.switchTo('albums', UNFILTERED);

    // Stepping into an album: the tracks start unfiltered ...
    expect(memory.switchTo('album-17', rated4Plus)).toEqual(UNFILTERED);
    // ... and going back up restores what was set for the album list.
    expect(memory.switchTo('albums', UNFILTERED)).toEqual(rated4Plus);
  });

  it('keeps the narrowing of a listing apart from search hits', () => {
    const memory = new BrowseFilterMemory();
    memory.switchTo('albums', UNFILTERED);

    expect(memory.switchTo('search_result', rated4Plus)).toEqual(UNFILTERED);
    expect(memory.switchTo('albums', UNFILTERED)).toEqual(rated4Plus);
  });

  it('forgets a listing whose narrowing was cleared again', () => {
    const memory = new BrowseFilterMemory();
    memory.switchTo('albums', UNFILTERED);
    memory.switchTo('album-17', rated4Plus);
    // Back on the album list, the user clears the filter, then leaves again.
    memory.switchTo('albums', UNFILTERED);
    memory.switchTo('album-17', UNFILTERED);

    expect(memory.switchTo('albums', UNFILTERED)).toEqual(UNFILTERED);
  });
});

describe('isUnfiltered', () => {
  it('recognizes the neutral state', () => {
    expect(isUnfiltered(UNFILTERED)).toBe(true);
    expect(isUnfiltered({ ...UNFILTERED, genres: [] })).toBe(true);
  });

  it('recognizes every criterion that narrows a listing', () => {
    expect(isUnfiltered({ ...UNFILTERED, quickSearch: 'live' })).toBe(false);
    expect(isUnfiltered({ ...UNFILTERED, genres: ['Jazz'] })).toBe(false);
    expect(isUnfiltered({ ...UNFILTERED, sort: 'TITLE' })).toBe(false);
    expect(isUnfiltered({ ...UNFILTERED, rating: '4' })).toBe(false);
  });
});

describe('splitGenres', () => {
  it('splits the separators used by media servers and omits empty values', () => {
    expect(splitGenres('Jazz / Jazz Rock;;;Alternative Rock;;;')).toEqual([
      'Jazz',
      'Jazz Rock',
      'Alternative Rock',
    ]);
  });
});

describe('filterMusicItems', () => {
  it('matches a genre separated by triple semicolons', () => {
    const dtoGenerator = new DtoGeneratorService();
    const musicItem = dtoGenerator.emptyMusicItem();
    musicItem.genre = 'Jazz;;;Jazz Rock;;;Alternative Rock';
    const container = dtoGenerator.generateEmptyContainerDto();
    container.genre = musicItem.genre;

    expect(
      filterMusicItems([musicItem], '', ['Alternative Rock'], 'ANY'),
    ).toEqual([musicItem]);
    expect(
      filterContainers([container], '', ['Alternative Rock'], 'ANY'),
    ).toEqual([container]);
  });
});
