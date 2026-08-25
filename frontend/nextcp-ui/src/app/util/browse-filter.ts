import { ContainerDto, MusicItemDto } from 'src/app/service/dto';
import {
  matchesRatingFilter,
  RatingFilter,
} from 'src/app/service/rating-service.service';

/**
 * Case insensitive substring match used by all browse filters. An entry without a
 * title only matches the neutral value 'NONE', which the genre filter uses to mean
 * "unset".
 */
export function matchesTextFilter(
  title: string | undefined,
  filter?: string,
): boolean {
  if (!filter) {
    return true;
  }
  if (!title) {
    return 'NONE' == filter;
  }
  return title.toLowerCase().includes(filter.toLowerCase());
}

/**
 * Narrows a container listing by the criteria of the browse header.
 *
 * Shared by the tile that renders the listing and by the section heading that counts
 * it. Counting through the same function is what keeps the heading from claiming a
 * number the grid does not show.
 */
export function filterContainers(
  containers: ContainerDto[] | undefined,
  quickSearch: string | undefined,
  genres: string[] | undefined,
  rating: RatingFilter,
): ContainerDto[] {
  let result = containers ?? [];
  if (quickSearch) {
    result = result.filter((item) => matchesTextFilter(item.title, quickSearch));
  }
  if (genres?.length) {
    result = result.filter((item) =>
      genres.some((genre) => matchesTextFilter(item.genre, genre)),
    );
  }
  if (rating !== 'ANY') {
    result = result.filter((item) => matchesRatingFilter(item.rating, rating));
  }
  return result;
}

/**
 * Narrows a track listing by the criteria of the browse header.
 *
 * Shared by the track list and by the play / shuffle / add-to-queue actions of the
 * header: what gets sent to the renderer has to be what the list shows, so both have
 * to come out of the same function.
 */
export function filterMusicItems(
  items: MusicItemDto[] | undefined,
  quickSearch: string | undefined,
  genres: string[] | undefined,
  rating: RatingFilter,
): MusicItemDto[] {
  let result = items ?? [];
  if (quickSearch) {
    result = result.filter((item) => matchesTextFilter(item.title, quickSearch));
  }
  if (genres?.length) {
    result = result.filter((item) =>
      genres.some((genre) => matchesTextFilter(item.genre, genre)),
    );
  }
  if (rating !== 'ANY') {
    result = result.filter((item) => matchesRatingFilter(item.rating, rating));
  }
  return result;
}

/** The four criteria of the browse header, kept together so they can be parked and restored as one. */
export interface BrowseFilterState {
  quickSearch: string;
  genres: string[];
  sort: string;
  rating: RatingFilter;
}

/** Nothing narrowed: what a listing starts from when it is seen for the first time. */
export const UNFILTERED: BrowseFilterState = {
  quickSearch: '',
  genres: [],
  sort: 'NONE',
  rating: 'ANY',
};

export function isUnfiltered(state: BrowseFilterState): boolean {
  return (
    state.quickSearch === UNFILTERED.quickSearch &&
    state.genres.length === 0 &&
    state.sort === UNFILTERED.sort &&
    state.rating === UNFILTERED.rating
  );
}

/**
 * Remembers the narrowing per listing.
 *
 * One browse view shows the album list, the tracks of an album and the hits of a global search one
 * after the other. A filter belongs to the listing it was set in: carrying it across them hides
 * things nobody asked to hide - a "4+" rating matches albums, but the tracks inside them usually
 * carry no rating of their own, so stepping into an album would show an empty track list. Dropping it
 * instead would lose the narrowing the moment the user looks into one of the results, so it is parked
 * and comes back when its listing does.
 */
export class BrowseFilterMemory {
  private readonly byContainer = new Map<string, BrowseFilterState>();
  private currentId: string | undefined;

  /**
   * Switches to another listing.
   *
   * @param containerId the listing now on screen
   * @param current the filters currently in effect, which belong to the previous listing
   * @returns the filters to apply, or undefined when this is still the same listing
   */
  public switchTo(
    containerId: string,
    current: BrowseFilterState,
  ): BrowseFilterState | undefined {
    if (containerId === this.currentId) {
      return undefined;
    }
    const previousId = this.currentId;
    this.currentId = containerId;
    if (previousId !== undefined) {
      // Only listings that are actually narrowed are worth remembering.
      if (isUnfiltered(current)) {
        this.byContainer.delete(previousId);
      } else {
        this.byContainer.set(previousId, current);
      }
    }
    return this.byContainer.get(containerId) ?? UNFILTERED;
  }
}
