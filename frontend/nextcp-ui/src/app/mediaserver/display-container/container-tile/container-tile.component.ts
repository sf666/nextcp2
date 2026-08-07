import {
  afterNextRender,
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
import { NgTemplateOutlet } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ContainerDto } from 'src/app/service/dto';
import { DeviceService } from 'src/app/service/device.service';
import {
  RATING_LIKED,
  RatingFilter,
} from 'src/app/service/rating-service.service';
import { filterContainers } from 'src/app/util/browse-filter';
import { ContainerRatingComponent } from '../../popup/container-rating/container-rating.component';

@Component({
  selector: 'container-tile',
  standalone: true,
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
  // Enable windowing (virtual scroll) for this grid. Only honoured for the flat
  // single-section case (NONE/TITLE). Set true only on the (large) album grid.
  virtualize = input<boolean>(false);
  containerList = computed(() => this.filteredContainer(this.container()));

  // Containers grouped into sections according to sortCriteria.
  // A section with an empty key is rendered without a header.
  groupedContainers = computed(() =>
    this.groupAndSort(this.containerList(), this.sortCriteria())
  );

  browseClicked = output<ContainerDto>();

  //
  // Rating by long press
  // ============================================================================
  // A tap on a tile navigates into the container, so the only spare gesture for
  // rating is a long press. The press is cancelled as soon as the finger moves,
  // otherwise it would fire while scrolling the grid.

  private readonly dialog = inject(MatDialog);
  private readonly deviceService = inject(DeviceService);
  private readonly LONG_PRESS_MS = 500;
  private readonly MOVE_TOLERANCE_PX = 10;

  private pressTimer: ReturnType<typeof setTimeout> | undefined;
  private pressStart: { x: number; y: number } | undefined;
  private pressHandled = false;

  ratingPossible(): boolean {
    return this.deviceService.selectedMediaServerDevice().extendedApi;
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
    if (!this.ratingPossible()) {
      return;
    }
    this.cancelPress();
    this.pressHandled = false;
    this.pressStart = { x: event.clientX, y: event.clientY };
    this.pressTimer = setTimeout(() => {
      this.pressHandled = true;
      this.openRatingDialog(container);
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
   * Same sheet as the long press, reached by tapping the tile's options button.
   * Stops the event so the tile does not also navigate into the container.
   */
  openOptions(event: Event, container: ContainerDto): void {
    event.stopPropagation();
    event.preventDefault();
    this.cancelPress();
    this.openRatingDialog(container);
  }

  private openRatingDialog(container: ContainerDto): void {
    const dialogRef = this.dialog.open(ContainerRatingComponent, {
      data: { container: container, rating: this.effectiveRating(container) },
      panelClass: ['popup', 'popup-glass'],
    });
    dialogRef.afterClosed().subscribe((newRating) => {
      if (newRating !== undefined) {
        // Reflect the new state on the tile without re-browsing.
        const next = new Map(this.ratingOverrides());
        next.set(container.id, newRating === null ? undefined : newRating);
        this.ratingOverrides.set(next);
      }
    });
  }

  //
  // Virtual scrolling (windowing)
  // ============================================================================
  // Only the visible rows are rendered into the DOM; a top and a bottom spacer
  // preserve the total scroll height. Coupled to the page-level #mainContent
  // scroll (NOT a nested scroller) so the collapsing header keeps working.
  // Scoped to the flat single-section case — grouped views render fully.

  private readonly destroyRef = inject(DestroyRef);
  private readonly BUFFER_ROWS = 3;
  private readonly ROW_H_FALLBACK = 300; // px, until a real tile is measured
  // Land a restored album this far below the top, clearing the collapsed header.
  private readonly RESTORE_TOP_MARGIN = 120;

  private vgrid = viewChild<ElementRef<HTMLElement>>('vgrid');
  private vwrap = viewChild<ElementRef<HTMLElement>>('vwrap');

  rowH = signal<number>(this.ROW_H_FALLBACK);
  cols = signal<number>(1);
  firstRow = signal<number>(0);
  lastRow = signal<number>(0);

  // Windowing is active only for a single flat section (no group headers).
  virtualActive = computed(
    () => this.virtualize() && this.groupedContainers().length === 1,
  );
  private flatItems = computed<ContainerDto[]>(() =>
    this.virtualActive() ? this.groupedContainers()[0].items : [],
  );
  totalRows = computed(() =>
    this.cols() > 0 ? Math.ceil(this.flatItems().length / this.cols()) : 0,
  );
  visibleItems = computed(() =>
    this.flatItems().slice(
      this.firstRow() * this.cols(),
      this.lastRow() * this.cols(),
    ),
  );
  spacerTopPx = computed(() => this.firstRow() * this.rowH());
  spacerBottomPx = computed(() =>
    Math.max(0, this.totalRows() - this.lastRow()) * this.rowH(),
  );

  private scrollParent: HTMLElement | null = null;
  private resizeObs: ResizeObserver | null = null;
  private rafScheduled = false;
  private pendingFocusId: string | null = null;
  private filterInitialized = false;

  private readonly onScroll = () => this.scheduleUpdate();

  constructor() {
    // Attach the page scroll listener once the view exists.
    afterNextRender(() => {
      this.scrollParent = document.getElementById('mainContent');
      this.scrollParent?.addEventListener('scroll', this.onScroll, {
        passive: true,
      });
      this.scheduleUpdate();
    });

    // (Re)attach a ResizeObserver on the window grid whenever it appears —
    // re-measures cols + rowH when the viewport width (column count) changes.
    effect(() => {
      const grid = this.vgrid()?.nativeElement;
      if (grid && !this.resizeObs && typeof ResizeObserver !== 'undefined') {
        this.resizeObs = new ResizeObserver(() => this.scheduleUpdate());
        this.resizeObs.observe(grid);
      }
    });

    // When the list (browse/filter) or the active flag changes, reset to the
    // top and drop any pending restore from a previous list, then remeasure.
    effect(() => {
      this.flatItems().length; // track
      this.virtualActive(); // track
      this.pendingFocusId = null;
      this.firstRow.set(0);
      this.scheduleUpdate();
    });

    // When the filter / genre selection changes, scroll back to the top so the
    // filtered results start from the beginning (not at the previous scroll
    // position). Scroll to page top so the header — which holds the filter
    // input — stays fully visible while typing.
    effect(() => {
      this.quickSearchString(); // track
      this.selectedGenres(); // track
      if (!this.filterInitialized) {
        this.filterInitialized = true;
        return;
      }
      if (!this.virtualActive()) {
        return;
      }
      this.firstRow.set(0);
      const parent = this.scrollParent ?? document.getElementById('mainContent');
      if (parent) {
        parent.scrollTop = 0;
      }
      this.scheduleUpdate();
    });

    this.destroyRef.onDestroy(() => {
      this.scrollParent?.removeEventListener('scroll', this.onScroll);
      this.resizeObs?.disconnect();
    });
  }

  // Coalesce scroll/resize/data updates into one measurement per frame.
  private scheduleUpdate(): void {
    if (this.rafScheduled) {
      return;
    }
    this.rafScheduled = true;
    requestAnimationFrame(() => {
      this.rafScheduled = false;
      this.measureAndRecompute();
    });
  }

  private measure(): void {
    const grid = this.vgrid()?.nativeElement;
    if (!grid) {
      return;
    }
    const cs = getComputedStyle(grid);
    // auto-fill columns resolve to a concrete list; count them (exact).
    const template = cs.gridTemplateColumns;
    if (template && template !== 'none') {
      const c = template.split(' ').filter((s) => s.length > 0).length;
      if (c > 0) {
        this.cols.set(c);
      }
    }
    // Row height from the first rendered tile (art has aspect-ratio, so its
    // height is known before images load); include the grid's row gap.
    const tile = grid.querySelector('.albumTile') as HTMLElement | null;
    if (tile) {
      const gap = parseFloat(cs.rowGap) || 0;
      const h = tile.offsetHeight + gap;
      if (h > 0) {
        this.rowH.set(h);
      }
    }
  }

  private recomputeWindow(): void {
    if (!this.virtualActive()) {
      return;
    }
    const wrap = this.vwrap()?.nativeElement;
    const parent =
      this.scrollParent ?? document.getElementById('mainContent');
    if (!wrap || !parent) {
      return;
    }
    const rowH = this.rowH() || this.ROW_H_FALLBACK;
    const total = this.totalRows();
    const totalHeight = total * rowH;
    // Distance scrolled past the top of this grid section. getBoundingClientRect
    // already accounts for the current (collapsing) header height, so no offset
    // arithmetic against --collapse is needed.
    const startPx = Math.min(
      Math.max(parent.getBoundingClientRect().top - wrap.getBoundingClientRect().top, 0),
      Math.max(0, totalHeight),
    );
    const first = Math.max(0, Math.floor(startPx / rowH) - this.BUFFER_ROWS);
    const visRows = Math.ceil(parent.clientHeight / rowH);
    const last = Math.min(total, first + visRows + this.BUFFER_ROWS * 2);
    this.firstRow.set(first);
    this.lastRow.set(last);
  }

  private measureAndRecompute(): void {
    if (!this.virtualActive()) {
      return;
    }
    this.measure();
    this.recomputeWindow();
    // If a restore is pending, the target row should now be rendered — correct
    // to the exact position using the real element on the next frame.
    if (this.pendingFocusId) {
      requestAnimationFrame(() => this.correctRestore());
    }
  }

  private correctRestore(): void {
    const id = this.pendingFocusId;
    if (!id) {
      return;
    }
    const el = document.getElementById(id);
    if (el) {
      el.scrollIntoView({ block: 'start' });
      this.pendingFocusId = null;
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
    if (!this.virtualActive()) {
      return false;
    }
    const idx = this.flatItems().findIndex((c) => c.id === id);
    if (idx < 0) {
      return false;
    }
    const wrap = this.vwrap()?.nativeElement;
    const parent =
      this.scrollParent ?? document.getElementById('mainContent');
    if (!wrap || !parent) {
      return false;
    }
    const rowH = this.rowH() || this.ROW_H_FALLBACK;
    const rowIndex = Math.floor(idx / (this.cols() || 1));
    // Absolute offset of this grid section within the scroll content.
    const wrapOffset =
      parent.scrollTop +
      (wrap.getBoundingClientRect().top - parent.getBoundingClientRect().top);
    parent.scrollTop = Math.max(
      0,
      wrapOffset + rowIndex * rowH - this.RESTORE_TOP_MARGIN,
    );
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
      const genre = item.genre?.split('/')[0]?.trim();
      return genre || 'Unknown';
    }
    return '';
  }

  private compareText(a?: string, b?: string): number {
    return (a ?? '').localeCompare(b ?? '', undefined, { sensitivity: 'base' });
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
