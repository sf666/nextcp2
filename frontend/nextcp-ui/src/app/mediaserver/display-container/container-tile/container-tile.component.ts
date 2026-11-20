import {
  ChangeDetectionStrategy,
  Component,
  computed,
  DestroyRef,
  effect,
  ElementRef,
  inject,
  input,
  output,
  signal,
  viewChild,
} from '@angular/core';
import { DOCUMENT, NgTemplateOutlet } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ContainerDto } from 'src/app/service/dto';
import { DeviceService } from 'src/app/service/device.service';
import { ServerFeature } from 'src/app/service/server-feature';
import {
  RATING_LIKED,
  RatingFilter,
  RatingServiceService,
} from 'src/app/service/rating-service.service';
import { filterContainers, splitGenres } from 'src/app/util/browse-filter';
import { AlbumArtService } from 'src/app/util/album-art.service';
import { DisplayHeaderOptionsComponent } from '../../popup/display-header-options/display-header-options.component';
import { BROWSE_WINDOW_THRESHOLD } from '../browse-window.directive';
import { browseGridRowAt, layoutBrowseGrid } from '../browse-grid-layout';

@Component({
  selector: 'container-tile',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [NgTemplateOutlet],
  templateUrl: './container-tile.component.html',
  styleUrl: './container-tile.component.scss',
})
export class ContainerTileComponent {
  container = input.required<ContainerDto[]>();
  smallIcons = input<boolean>(false);
  showPlayOverlay = input<boolean>(false);
  quickSearchString = input<string>("");
  selectedGenres = input<Array<string>>([]);
  // Narrows the listing down by rating.
  ratingFilter = input<RatingFilter>('ANY');
  // Sort/group criteria: 'NONE' | 'TITLE' | 'ARTIST' | 'GENRE'
  sortCriteria = input<string>('NONE');
  listingContainer = input<ContainerDto>();
  containerList = computed(() => this.filteredContainer(this.container()));

  // Containers grouped into sections according to sortCriteria.
  // A section with an empty key is rendered without a header.
  groupedContainers = computed(() =>
    this.groupAndSort(this.containerList(), this.sortCriteria())
  );

  browseClicked = output<ContainerDto>();
  /** Handed to the options menu, which emits on it - the tile itself has no queue to add to. */
  addToPlaylistClicked = output<ContainerDto>();

  //
  // Options menu, by long press or by the tile's options button
  // ============================================================================
  // A tap on a tile navigates into the container, so the only spare gesture for
  // the menu is a long press. The press is cancelled as soon as the finger moves,
  // otherwise it would fire while scrolling the grid.

  private readonly dialog = inject(MatDialog);
  readonly albumArt = inject(AlbumArtService);
  private readonly deviceService = inject(DeviceService);
  private readonly ratingService = inject(RatingServiceService);
  private readonly LONG_PRESS_MS = 500;
  private readonly MOVE_TOLERANCE_PX = 10;

  private pressTimer: ReturnType<typeof setTimeout> | undefined;
  private pressStart: { x: number; y: number } | undefined;
  private pressHandled = false;

  private canLike(): boolean {
    return this.deviceService.hasFeature(ServerFeature.ALBUM_LIKES);
  }

  optionsPossible(container: ContainerDto): boolean {
    const server = this.deviceService.selectedMediaServerDevice();
    return this.canLike() || (
      !!container.id && container.id !== '0' &&
      this.parentObjectClass(container)?.startsWith('object.container.playlistContainer') === true &&
      server.extendedApi && server.udn === container.mediaServerUDN
    );
  }

  // Ratings changed in this view. The browse result DTOs are plain objects, so
  // mutating them would not repaint an OnPush component.
  private ratingOverrides = signal<Map<string, number | undefined>>(new Map());

  /**
   * What this container is rated right now, as far as this view knows.
   *
   * The browse DTO is only the state at load time; anything rated since then
   * lives in the override map. The media server rejects an update whose stated
   * previous value does not match its own, so this — not CONTAINER.RATING — is
   * what must be sent along.
   */
  private effectiveRating(container: ContainerDto): number | undefined {
    const overrides = this.ratingOverrides();
    return overrides.has(container.id)
      ? overrides.get(container.id)
      : (container.rating ?? undefined);
  }

  isLiked(container: ContainerDto): boolean {
    return this.effectiveRating(container) === RATING_LIKED;
  }

  onPressStart(event: PointerEvent, container: ContainerDto): void {
    if (!this.optionsPossible(container)) {
      return;
    }
    this.cancelPress();
    this.pressHandled = false;
    this.pressStart = { x: event.clientX, y: event.clientY };
    const trigger = event.currentTarget;
    this.pressTimer = setTimeout(() => {
      this.pressHandled = true;
      this.openOptionsMenu(container, trigger);
    }, this.LONG_PRESS_MS);
  }

  onPressMove(event: PointerEvent): void {
    if (!this.pressStart) {
      return;
    }
    const dx = Math.abs(event.clientX - this.pressStart.x);
    const dy = Math.abs(event.clientY - this.pressStart.y);
    if (dx > this.MOVE_TOLERANCE_PX || dy > this.MOVE_TOLERANCE_PX) {
      this.cancelPress();
    }
  }

  onPressEnd(): void {
    this.cancelPress();
  }

  private cancelPress(): void {
    if (this.pressTimer !== undefined) {
      clearTimeout(this.pressTimer);
      this.pressTimer = undefined;
    }
    this.pressStart = undefined;
  }

  /**
   * Same menu as the long press, reached by tapping the tile's options button.
   * Stops the event so the tile does not also navigate into the container.
   */
  openOptions(event: Event, container: ContainerDto): void {
    event.stopPropagation();
    event.preventDefault();
    this.cancelPress();
    this.openOptionsMenu(container, event.currentTarget);
  }

  private parentObjectClass(container: ContainerDto): string | undefined {
    const listing = this.listingContainer();
    return listing && listing.id === container.parentID &&
      listing.mediaServerUDN === container.mediaServerUDN ? listing.objectClass : undefined;
  }

  /**
   * Opens the shared container menu beside the options button or the long-pressed tile.
   */
  private openOptionsMenu(container: ContainerDto, trigger: EventTarget | null): void {
    const dialogRef = this.dialog.open(DisplayHeaderOptionsComponent, {
      hasBackdrop: true,
      panelClass: ['popup-glass'],
      data: {
        trigger: new ElementRef(trigger),
        addToPlaylistOutput: this.addToPlaylistClicked,
        currentContainer: container,
        parentObjectClass: this.parentObjectClass(container),
        // What is on screen is the container this one is listed in, so that is what a change here
        // has to re-read.
        listingContainerId: container.parentID,
        canLike: this.canLike(),
        isLiked: this.isLiked(container),
        // A tile knows the container, not what is in it - and "Set artist folder" is decided by the
        // number of folders inside. It stays with the header of the folder itself.
        childFolderCount: 0,
      },
    });
    // The menu only reports the choice; the rating call stays with the view that holds the entry.
    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'toggleLike') {
        this.toggleLike(container);
      }
    });
  }

  /**
   * Likes the container, or takes the like back - which clears the rating rather than storing a
   * dislike. Same two states as everywhere else in the app.
   */
  private toggleLike(container: ContainerDto): void {
    if (!this.canLike()) {
      return;
    }
    const previousRating = this.effectiveRating(container);
    const newRating = this.isLiked(container) ? undefined : RATING_LIKED;
    this.ratingService
      .setResourceRating(
        container.id,
        previousRating,
        newRating,
        container.parentID,
        container.objectClass,
      )
      .subscribe({
        next: () => {
          // Reflect the new state on the tile without re-browsing.
          const next = new Map(this.ratingOverrides());
          next.set(container.id, newRating);
          this.ratingOverrides.set(next);
        },
        error: (err) => console.error('cannot rate container', err),
      });
  }

  //
  // Virtual scrolling (windowing)
  private readonly destroyRef = inject(DestroyRef);
  private readonly scrollParent = inject(DOCUMENT).getElementById('mainContent');
  private readonly RESTORE_TOP_MARGIN = 120;
  private readonly BUFFER_ROWS = 3;

  private vwrap = viewChild<ElementRef<HTMLElement>>('vwrap');
  private headingMeasure = viewChild<ElementRef<HTMLElement>>('headingMeasure');

  protected readonly virtualActive = computed(() => this.scrollParent !== null &&
    this.container().length >= BROWSE_WINDOW_THRESHOLD);
  protected readonly reserveBadgeSpace = computed(() => this.virtualActive() &&
    this.container().some((item) => !this.isAlbum(item) && !!this.getOtherContainerHeadline(item)));
  private readonly geometry = signal({ columns: 1, tileHeight: 300, gap: 16, headingHeight: 60 }, {
    equal: (a, b) => a.columns === b.columns && a.tileHeight === b.tileHeight &&
      a.gap === b.gap && a.headingHeight === b.headingHeight,
  });
  private readonly range = signal({ first: 0, last: 2 }, {
    equal: (a, b) => a.first === b.first && a.last === b.last,
  });
  private readonly gridRows = computed(() => {
    const { columns, tileHeight, gap, headingHeight } = this.geometry();
    return this.virtualActive()
      ? layoutBrowseGrid(this.groupedContainers(), columns, tileHeight, gap, headingHeight)
      : [];
  });
  protected readonly visibleRows = computed(() => {
    const rows = this.gridRows();
    const { first, last } = this.range();
    return first < rows.length ? rows.slice(first, last) : rows.slice(0, 2);
  });
  protected readonly spacerTopPx = computed(() => this.visibleRows()[0]?.top ?? 0);
  protected readonly spacerBottomPx = computed(() =>
    (this.gridRows().at(-1)?.end ?? 0) - (this.visibleRows().at(-1)?.end ?? 0));

  private frame?: number;
  // Identity of the listing the window currently shows, so that a page being
  // appended can be told apart from a different list being browsed.
  private listAnchorId: string | undefined;
  private listLength = 0;
  private listCriteria = 'NONE';
  // Column count and row height only change when the grid is resized or the
  // list is swapped — never from scrolling. Measuring them reads computed style
  // and offsetHeight, which forces a synchronous layout, so it must not happen
  // once per scrolled frame. Set here, consumed by the next update.
  private needsMeasure = true;
  private pendingFocusId: string | null = null;
  private filterInitialized = false;

  private readonly onScroll = () => this.scheduleUpdate();

  constructor() {
    effect((onCleanup) => {
      const wrap = this.vwrap()?.nativeElement;
      const parent = this.scrollParent;
      if (!wrap || !parent) {
        return;
      }
      const resizeObserver = new ResizeObserver(() => this.scheduleUpdate(true));
      resizeObserver.observe(wrap);
      resizeObserver.observe(parent);
      parent.addEventListener('scroll', this.onScroll, { passive: true });
      this.scheduleUpdate(true);
      onCleanup(() => {
        resizeObserver.disconnect();
        parent.removeEventListener('scroll', this.onScroll);
      });
    });

    // A different listing starts at the top again. An appended page does not:
    // a browse loads its pages one after the other, and on a library-sized list
    // the user is already scrolling while the rest still arrives. Resetting the
    // window there yanked the view back to the first row on every page — which
    // is also why the covers being looked at kept being re-fetched instead of
    // the ones on screen.
    effect(() => {
      const raw = this.container();
      const criteria = this.sortCriteria();
      const active = this.virtualActive();
      // Pages are appended to the same listing, so its first entry stays put and
      // its length only grows. Anything else is a new list.
      const anchorId = raw.length > 0 ? raw[0].id : undefined;
      const appended =
        anchorId === this.listAnchorId &&
        raw.length >= this.listLength &&
        criteria === this.listCriteria;
      this.listAnchorId = anchorId;
      this.listLength = raw.length;
      this.listCriteria = criteria;
      if (!appended) {
        this.pendingFocusId = null;
        this.range.set({ first: 0, last: 2 });
      }
      if (active) {
        this.scheduleUpdate(true);
      }
    });

    // When the filter / genre selection changes, scroll back to the top so the
    // filtered results start from the beginning (not at the previous scroll
    // position). Scroll to page top so the header — which holds the filter
    // input — stays fully visible while typing.
    effect(() => {
      this.quickSearchString(); // track
      this.selectedGenres(); // track
      this.ratingFilter(); // track
      if (!this.filterInitialized) {
        this.filterInitialized = true;
        return;
      }
      if (!this.virtualActive()) {
        return;
      }
      this.range.set({ first: 0, last: 2 });
      const parent = this.scrollParent;
      if (parent) {
        parent.scrollTop = 0;
      }
      this.scheduleUpdate();
    });

    this.destroyRef.onDestroy(() => {
      if (this.frame !== undefined) {
        cancelAnimationFrame(this.frame);
      }
    });
  }

  // Coalesce scroll/resize/data updates into one window recompute per frame.
  // `remeasure` re-reads the grid geometry as well; scrolling must not.
  private scheduleUpdate(remeasure = false): void {
    if (remeasure) {
      this.needsMeasure = true;
    }
    if (this.frame !== undefined) {
      return;
    }
    this.frame = requestAnimationFrame(() => {
      this.frame = undefined;
      this.measureAndRecompute();
    });
  }

  /**
   * Reads column count and row height off the live grid. Returns false while the
   * grid holds no tile yet (nothing to measure), so the caller keeps asking
   * until the numbers are real instead of staying on the fallback row height.
   */
  private measure(): boolean {
    const grid = this.vwrap()?.nativeElement.querySelector<HTMLElement>('.tile-grid');
    const tile = grid?.firstElementChild;
    const header = this.headingMeasure()?.nativeElement;
    if (!grid || !tile || !header) {
      return false;
    }
    const tileHeight = tile.getBoundingClientRect().height;
    if (tileHeight <= 0) {
      return false;
    }
    const gridStyle = getComputedStyle(grid);
    const headingStyle = getComputedStyle(header);
    this.geometry.set({
      columns: Math.max(1, gridStyle.gridTemplateColumns.split(' ').length),
      tileHeight,
      gap: parseFloat(gridStyle.rowGap) || 0,
      headingHeight: header.getBoundingClientRect().height +
        (parseFloat(headingStyle.marginTop) || 0) + (parseFloat(headingStyle.marginBottom) || 0),
    });
    return true;
  }

  private measureAndRecompute(): void {
    const wrap = this.vwrap()?.nativeElement;
    const parent = this.scrollParent;
    if (!wrap || !parent) {
      return;
    }
    if (this.needsMeasure && this.measure()) {
      this.needsMeasure = false;
    }
    const rows = this.gridRows();
    let offset = parent.getBoundingClientRect().top - wrap.getBoundingClientRect().top;
    const target = this.pendingFocusId && !this.needsMeasure
      ? rows.find((row) => 'items' in row && row.items.some((item) => item.id === this.pendingFocusId))
      : undefined;
    if (target) {
      parent.scrollTop += target.top - offset - this.RESTORE_TOP_MARGIN;
      offset = parent.getBoundingClientRect().top - wrap.getBoundingClientRect().top;
      requestAnimationFrame(() => this.correctRestore());
    }
    const firstVisible = Math.min(browseGridRowAt(rows, offset), Math.max(0, rows.length - 1));
    const first = Math.max(0, firstVisible - this.BUFFER_ROWS);
    const last = Math.min(rows.length, Math.max(firstVisible, browseGridRowAt(rows,
      offset + parent.clientHeight)) + this.BUFFER_ROWS + 1);
    this.range.set({ first, last });
  }

  private correctRestore(): void {
    const id = this.pendingFocusId;
    if (!id || this.destroyRef.destroyed) {
      return;
    }
    const el = document.getElementById(id);
    if (el) {
      this.pendingFocusId = null;
      el.scrollIntoView({ block: 'start' });
    }
    // else: element not rendered yet — a later update() will retry.
  }

  /**
   * Restore scroll to a specific container id even when it is not currently in
   * the DOM (windowed). Estimates the scroll position to render the target row,
   * then a follow-up correction snaps to the exact element. Returns false if the
   * id is not part of this (virtualized) list, so the caller can fall back.
   */
  public scrollToId(id: string): boolean {
    if (!this.virtualActive() || !this.vwrap() || !this.scrollParent ||
      !this.containerList().some((item) => item.id === id)) {
      return false;
    }
    this.pendingFocusId = id;
    this.scheduleUpdate();
    return true;
  }

  private groupAndSort(
    items: ContainerDto[],
    criteria: string
  ): { key: string; items: ContainerDto[] }[] {
    // No sorting: keep original order, single section without header.
    if (!criteria || criteria === 'NONE') {
      return [{ key: '', items }];
    }

    // "Album Title" sorts alphabetically without section headers.
    if (criteria === 'TITLE') {
      const sorted = [...items].sort((a, b) =>
        this.compareText(a.title, b.title)
      );
      return [{ key: '', items: sorted }];
    }

    // "Album Artist" / "Genre" group into labelled sections.
    const groups = new Map<string, ContainerDto[]>();
    for (const item of items) {
      const key = this.groupKey(item, criteria);
      const bucket = groups.get(key);
      if (bucket) {
        bucket.push(item);
      } else {
        groups.set(key, [item]);
      }
    }

    const result = Array.from(groups.entries()).map(([key, groupItems]) => ({
      key,
      items: groupItems.sort((a, b) => this.compareText(a.title, b.title)),
    }));
    result.sort((a, b) => this.compareText(a.key, b.key));
    return result;
  }

  private groupKey(item: ContainerDto, criteria: string): string {
    if (criteria === 'ARTIST') {
      return item.artist?.trim() || 'Unknown';
    }
    if (criteria === 'GENRE') {
      // Use the first genre token only (e.g. "R&B / Soul" -> "R&B").
      const genre = splitGenres(item.genre)[0];
      return genre || 'Unknown';
    }
    return '';
  }

  // One collator for all comparisons. localeCompare with an options object builds
  // a fresh collator per call, which on a sort over a whole library (tens of
  // thousands of comparisons, repeated as each page comes in) cost far more than
  // the comparison itself.
  private static readonly COLLATOR = new Intl.Collator(undefined, {
    sensitivity: 'base',
  });

  private compareText(a?: string, b?: string): number {
    return ContainerTileComponent.COLLATOR.compare(a ?? '', b ?? '');
  }

  private filteredContainer(container: ContainerDto[]): ContainerDto[] {
    return filterContainers(
      container,
      this.quickSearchString(),
      this.selectedGenres(),
      this.ratingFilter()
    );
  }

  public browseTo(containerDto: ContainerDto): void {
    // Swallow the tap that follows a long press, so rating a tile does not also
    // navigate into it.
    if (this.pressHandled) {
      this.pressHandled = false;
      return;
    }
    this.browseClicked.emit(containerDto);
  }

  getSmallCss() {
    if (this.smallIcons()) {
      return ' small ';
    } else {
      return '';
    }
  }

  isAlbum(item: ContainerDto): boolean {
    return item.objectClass?.startsWith('object.container.album');
  }

  getOtherContainerHeadline(item: ContainerDto): string {
    if (item.objectClass.startsWith('object.container.person')) {
      return 'ARTIST';
    } else if (
      item.objectClass?.startsWith('object.container.playlistContainer')
    ) {
      return 'PLAYLIST';
    } else if (item.objectClass?.startsWith('object.container.album')) {
      return 'ALBUM';
    } else if (item.objectClass?.startsWith('object.container.genre')) {
      return 'GENRE';
    } else if (item.objectClass?.startsWith('object.container.channelGroup')) {
      return 'CHANNELS';
    } else if (item.objectClass?.startsWith('object.container.epgContainer')) {
      return 'EPG';
    } else if (item.objectClass?.startsWith('object.container.storageSystem')) {
      return 'DEVICE';
    } else if (item.objectClass?.startsWith('object.container.storageVolume')) {
      return 'DISC';
    } else if (item.objectClass?.startsWith('object.container.storageFolder')) {
      return '';
    } else if (
      item.objectClass?.startsWith('object.container.bookmarkFolder')
    ) {
      return 'BOOKMARKS';
    }
    return '';
  }
}
