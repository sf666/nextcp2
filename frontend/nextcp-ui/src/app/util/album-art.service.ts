import { Injectable, signal } from '@angular/core';

/**
 * Picks which of the album art variants a view should load.
 *
 * The media server offers the same cover in several sizes (see MusicItemDto.albumArtUrl /
 * albumArtUrlMedium / albumArtUrlLarge). A grid tile is only ~130-250 CSS px wide, so the small
 * variant is the right one - but a display with a device pixel ratio of 2 or 3 turns those into
 * 260-750 real pixels, which a 160x160 thumbnail cannot fill and visibly upscales. On such screens
 * the grid asks for the mid sized variant instead, which is why this decision is centralized here
 * rather than repeated in every template.
 */
@Injectable({
  providedIn: 'root',
})
export class AlbumArtService {
  /** True on high density displays, kept live because the ratio changes when a window moves screens. */
  public readonly highDensity = signal<boolean>(false);

  constructor() {
    if (typeof window === 'undefined' || !window.matchMedia) {
      return;
    }
    const query = window.matchMedia('(min-resolution: 2dppx)');
    this.highDensity.set(query.matches);
    // Zooming or dragging the window to another display changes the ratio; Safari < 14 has no
    // addEventListener on MediaQueryList, hence the guard.
    if (query.addEventListener) {
      query.addEventListener('change', (event) => this.highDensity.set(event.matches));
    }
  }

  /**
   * Artwork for a grid tile: the mid sized variant on a high density display, the small one otherwise.
   * Falls back to whatever is present, so servers that only ship one size keep working.
   */
  public tile(smallUrl: string | undefined, mediumUrl: string | undefined): string {
    if (this.highDensity() && mediumUrl) {
      return mediumUrl;
    }
    return smallUrl || mediumUrl || '';
  }

  /** Artwork for a single larger image (browse header, detail views): mid size regardless of density. */
  public detail(smallUrl: string | undefined, mediumUrl: string | undefined): string {
    return mediumUrl || smallUrl || '';
  }
}
