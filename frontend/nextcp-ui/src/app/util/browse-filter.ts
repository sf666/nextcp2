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
